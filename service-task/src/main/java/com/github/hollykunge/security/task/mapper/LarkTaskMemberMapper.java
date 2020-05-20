package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.common.vo.RpcUserInfo;
import com.github.hollykunge.security.task.dto.LarkProjectMemberDto;
import com.github.hollykunge.security.task.dto.LarkTaskMemberDto;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation 任务人员
 */
public interface LarkTaskMemberMapper extends Mapper<LarkTaskMember> {

    /**
     * 获取任务以及子任务的当前参与人员
     * @param taskCode
     * @return
     */
    List<LarkTaskMemberDto> getChildTaskUser(@Param("taskCode")String taskCode);

    void updateTaskMember(String modifyMemberId, String modifiedMemberId);
}