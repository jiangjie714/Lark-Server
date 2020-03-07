package com.github.hollykunge.security.task.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.task.entity.ProjectRole;
import com.github.hollykunge.security.task.service.ProjectRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 角色
 */
@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addRole(ProjectRole projectRole) {
        mongoTemplate.insert(projectRole,"task_project_auth");
    }

    @Override
    public TableResultResponse<ProjectRole> findRole(int pageNum, int pageSize) {
        Page<Object> result = PageHelper.startPage(pageNum,pageSize);
        List<ProjectRole> projectRoles = mongoTemplate.findAll(ProjectRole.class);
        return new TableResultResponse<ProjectRole>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), projectRoles);
    }
}
