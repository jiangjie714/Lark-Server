package com.github.hollykunge.security.task.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.task.entity.ProjectRole;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 角色
 */
public interface ProjectRoleService {

    /**
     * 新建角色
     * @param projectRole 新建角色信息
     */
    public void addRole(ProjectRole projectRole);

    TableResultResponse<ProjectRole> findRole(int pageNum, int pageSize);
}
