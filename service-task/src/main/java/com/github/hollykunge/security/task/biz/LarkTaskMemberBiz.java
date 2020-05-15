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
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskMemberBiz extends BaseBiz<LarkTaskMemberMapper, LarkTaskMember> {

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
        List<LarkProjectMemberDto> projectUserList = new ArrayList<>();
        List<LarkProjectMemberDto> projectUsers = larkProjectMemberMapper.getProjectUser(projectCode);
        List<LarkTaskMemberDto> taskUsers = larkTaskMemberMapper.getChildTaskUser(taskCode);
        List<String> users = larkTaskMemberMapper.getTaskUser(projectCode,taskCode);
        projectUsers.stream().forEach(projectUser -> {
            if (users.stream().anyMatch(userId -> projectUser.getMemberCode().equals(userId))) {
            } else {
                projectUserList.add(projectUser);
            }
        });
        List<Object> objects = new ArrayList<>();
        objects.add(projectUserList);
        objects.add(taskUsers);
        return new ObjectRestResponse<>().data(objects).rel(true);
    }

    /**
     * 更新任务执行人
     * @param modifyMemberId
     * @param modifiedMemberId
     * @return
     */
    public ObjectRestResponse<LarkTaskMember> updateTaskMember(String modifyMemberId, String modifiedMemberId) {
        larkTaskMemberMapper.updateTaskMember(modifyMemberId,modifiedMemberId);
        return new ObjectRestResponse<>().rel(true);
    }
}
