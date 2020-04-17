package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.UserInvalidException;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.vo.mq.NoticeVO;
import com.github.hollykunge.security.entity.Notice;
import com.github.hollykunge.security.mapper.NoticeMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description: 工作台
 * @author: dd
 * @since: 2019-06-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeService extends BaseBiz<NoticeMapper, Notice> {
    @Override
    protected String getPageName() {
        return null;
    }

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * fansq
     * 20-2-17
     * 查询所有系统公告
     * @param orgCode
     * @return
     */
    public List<Notice> selectNoticList(String orgCode){
        Notice notice = new Notice();
        notice.setOrgCode(orgCode);
        if (StringUtils.isEmpty(orgCode)) {
            throw new UserInvalidException("当前登录人员无组织编码。");
        }
        Example example = new Example(Notice.class);
        example.setOrderByClause("SEND_TIME DESC");
        List<Notice> notices = mapper.selectByExample(example);
        notices = notices.stream().filter(new Predicate<Notice>() {
            @Override
            public boolean test(Notice notice) {
                //组织编码为空的不显示
                if (StringUtils.isEmpty(notice.getOrgCode())) {
                    return false;
                }
                if(org.apache.commons.lang3.StringUtils.equals(notice.getType(),"system")){
                    return true;
                }
                return false;
            }
        }).collect(Collectors.toList());
        Collections.sort(notices);
        return notices;
    }

    /**
     * fansq
     * 20-2-17
     * 查询除去系统公告  属于本部门的密级小于当前登录人的公告
     * @param orgCode
     * @param userSecretLevel
     * @return
     */
    public List<Notice> selectNotic(String orgCode,String userSecretLevel){
        Notice entity = new Notice();
        entity.setOrgCode(orgCode);
        if (StringUtils.isEmpty(orgCode)) {
            throw new BaseException("当前登录人没有组织编码...");
        }
        Example example = new Example(Notice.class);
        example.setOrderByClause("SEND_TIME DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("type","system");
        List<Notice> notices = mapper.selectByExample(example);
        notices = notices.stream().filter(new Predicate<Notice>() {
            @Override
            public boolean test(Notice notice) {
                //组织编码为空的不显示
                if (StringUtils.isEmpty(notice.getOrgCode())) {
                    return false;
                }
                if (entity.getOrgCode().contains(notice.getOrgCode())) {
                    //密级小于当前人的密级显示
                    if (isShow(userSecretLevel,notice.getSecretLevel())) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }).collect(Collectors.toList());
        Collections.sort(notices);
        return notices;
    }

    public List<Notice> selectList(Notice entity, String userSecretLevel) {
        if (StringUtils.isEmpty(entity.getOrgCode())) {
            throw new BaseException("当前登录人没有组织编码...");
        }
        Example example = new Example(Notice.class);
        example.setOrderByClause("SEND_TIME DESC");
        List<Notice> notices = mapper.selectByExample(example);
        notices = notices.stream().filter(new Predicate<Notice>() {
            @Override
            public boolean test(Notice notice) {
                //组织编码为空的不显示
                if (StringUtils.isEmpty(notice.getOrgCode())) {
                    return false;
                }
                if (entity.getOrgCode().contains(notice.getOrgCode())) {
                    //密级小于当前人的密级显示
                    if (isShow(userSecretLevel,notice.getSecretLevel())) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }).collect(Collectors.toList());
        Collections.sort(notices);
        return notices;
    }

    private boolean isShow(String userSecretLevel, String noticeSecretLevel) {
        if (StringUtils.isEmpty(userSecretLevel) || StringUtils.isEmpty(noticeSecretLevel)) {
            return false;
        }
        int userLevel = Integer.parseInt(userSecretLevel);
        int noticeLevel = Integer.parseInt(noticeSecretLevel);
        if (userLevel >= noticeLevel) {
            return true;
        }
        return false;
    }

    public TableResultResponse<Notice> page(Query query,String userSecretLevel,String orgCode) {
        Example example = new Example(Notice.class);
        if(query.entrySet().size()>0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        example.setOrderByClause("SEND_TIME DESC");
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Notice> list = mapper.selectByExample(example);
        list = list.stream().filter(new Predicate<Notice>() {
            @Override
            public boolean test(Notice notice) {
                //组织编码为空的不显示
                if (StringUtils.isEmpty(notice.getOrgCode())) {
                    return false;
                }
                if (orgCode.contains(notice.getOrgCode())) {
                    //密级小于当前人的密级显示
                    if (isShow(userSecretLevel,notice.getSecretLevel())) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }).collect(Collectors.toList());
        return new TableResultResponse<Notice>(result.getPageSize(), result.getPageNum() ,result.getPages(), result.getTotal(), list);
    }

    /**
     * fansq
     * 20-2-19
     * 删除指定公告
     * @param noticeVO 参数公告实体
     * @return count
     */
    public int deleteNoticByFromId(NoticeVO noticeVO){
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeVO,notice);
        Example example = new Example(Notice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fromId",notice.getFromId());
        return noticeMapper.deleteByExample(example);
    }

}
