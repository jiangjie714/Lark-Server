package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.dto.LarkProjectMemberDto;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.entity.LarkProjectMember;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkProjectMemberMapper extends Mapper<LarkProjectMember> {

    /**
     * 查询项目参与人员id
     * 可以使用tk-mybatis 这里自己写sql 减少for循环
     * @param projectCode
     * @return
     */
    List<String> getProjectUserId(@Param("projectCode") String projectCode);

    /**
     * 获取项目人员全部信息  包含用户头像等 给任务提供人员邀请信息
     * @param projectCode
     * @return
     *
     */
    List<LarkProjectMemberDto> getProjectUser(@Param("projectCode") String projectCode);

}