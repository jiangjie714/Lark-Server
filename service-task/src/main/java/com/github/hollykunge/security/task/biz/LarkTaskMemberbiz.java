package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import com.github.hollykunge.security.task.mapper.LarkTaskMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskMemberbiz extends BaseBiz<LarkTaskMemberMapper, LarkTaskMember> {

    @Autowired
    private LarkTaskMemberMapper larkTaskMemberMapper;
    @Override
    protected String getPageName() {
        return null;
    }
}
