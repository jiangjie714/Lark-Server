package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.vo.RpcUserInfo;
import com.github.hollykunge.security.task.dto.LarkProjectMemberDto;
import com.github.hollykunge.security.task.dto.LarkTaskMemberDto;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import com.github.hollykunge.security.task.mapper.LarkProjectMemberMapper;
import com.github.hollykunge.security.task.mapper.LarkTaskMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskMemberbiz extends BaseBiz<LarkTaskMemberMapper, LarkTaskMember> {

    @Autowired
    private LarkTaskMemberMapper larkTaskMemberMapper;

    @Autowired
    private LarkProjectMemberMapper larkProjectMemberMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 查询任务邀请人
     * 返回1.项目参与人  2.子任务参与人
     * @param projectCode
     * @param taskCode
     * @return
     */
    public ObjectRestResponse<List<Object>> getProjectUser(String projectCode, String taskCode) {
        List<LarkProjectMemberDto> projectUsers = larkProjectMemberMapper.getProjectUser(projectCode);
        List<LarkTaskMemberDto> taskUsers = larkTaskMemberMapper.getChildTaskUser(taskCode);
        List<Object> objects = new ArrayList<>();
        objects.add(projectUsers);
        objects.add(taskUsers);
        return new ObjectRestResponse<>().data(objects).rel(true);
    }
}
