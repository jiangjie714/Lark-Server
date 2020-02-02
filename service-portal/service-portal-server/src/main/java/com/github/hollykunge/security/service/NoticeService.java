package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.entity.Notice;
import com.github.hollykunge.security.mapper.NoticeMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
}
