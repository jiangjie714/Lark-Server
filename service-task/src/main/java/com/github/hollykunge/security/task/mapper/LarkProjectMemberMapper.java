package com.github.hollykunge.security.task.mapper;

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
     * 查询项目参与人员 可以使用tk-mybatis 这里自己写sql 减少for循环
     * @param projectCode
     * @return
     */
    List<String> getProjectUserId(@Param("projectCode") String projectCode);

}