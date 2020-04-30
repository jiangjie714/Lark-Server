package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.common.vo.RpcUserInfo;
import com.github.hollykunge.security.task.dto.LarkTaskMemberDto;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkTaskMemberMapper extends Mapper<LarkTaskMember> {
    List<LarkTaskMemberDto> getProjectUser(String projectCode);

    List<LarkTaskMemberDto> getChildTaskUser(String taskCode);
}