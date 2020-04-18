package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.config.mq.ProduceSenderConfig;
import com.github.hollykunge.security.admin.entity.Notice;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.NoticeMapper;

import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.FrontInputException;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.vo.mq.NoticeVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author: yzq
 * @Date: 创建于 2019/6/4 16:09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeBiz extends BaseBiz<NoticeMapper, Notice> {
    @Autowired
    private ProduceSenderConfig produceSenderConfig;

    @Resource
    private UserMapper userMapper;

    public String getOrgIdByUserId(String userId) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id", userId);
        List<User> orgUser = userMapper.selectByExample(example);

        return orgUser.get(0).getOrgCode();

    }

    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    public void insertSelective(Notice entity) {
        entity.setIsSend("0");
        super.insertSelective(entity);
    }

    /**
     * 发布消息
     *
     * @param entity
     */
    public void sentNotice(Notice entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            throw new FrontInputException("该通知已不存在。");
        }
        entity.setIsSend("1");
        mapper.updateByPrimaryKeySelective(entity);
        //保存完成后向mq发送一条消息
        NoticeVO mqNoticeEntity = new NoticeVO();
        BeanUtils.copyProperties(entity, mqNoticeEntity);
        //发布时间取当前时间
        mqNoticeEntity.setSendTime(new Date());
        mqNoticeEntity.setFromId(entity.getId());
        produceSenderConfig.send(mqNoticeEntity.getId(), mqNoticeEntity);
    }

    /**
     * fansq
     * 20-2-18
     * 取消发布公告
     *
     * @param id 主键id
     */
    public void sentCancelNotice(String id) {
        Notice entity = new Notice();
        if (StringUtils.isEmpty(id)) {
            throw new FrontInputException("该通知已不存在。");
        }
        entity.setId(id);
        entity.setIsSend("0");
        mapper.updateByPrimaryKeySelective(entity);
        //保存完成后向mq发送一条消息
        NoticeVO mqNoticeEntity = new NoticeVO();
        BeanUtils.copyProperties(entity, mqNoticeEntity);
        mqNoticeEntity.setFromId(entity.getId());
        produceSenderConfig.sendCancelPortal(mqNoticeEntity.getId(), mqNoticeEntity);
    }

    public TableResultResponse<Notice> pageList(Query query, String userId) {
        if(StringUtils.isEmpty(userId)){
            throw new FrontInputException("没有当前用户。");
        }
        Class<Notice> clazz = (Class<Notice>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        criteria.andEqualTo("crtUser", userId);
        example.setOrderByClause("CRT_TIME DESC");
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Notice> list = mapper.selectByExample(example);
        return new TableResultResponse<Notice>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), list);
    }
}
