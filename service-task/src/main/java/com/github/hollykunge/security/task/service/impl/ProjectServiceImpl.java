package com.github.hollykunge.security.task.service.impl;

import com.github.hollykunge.security.task.entity.Project;
import com.github.hollykunge.security.task.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 项目service
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createProject(Project project) {
        mongoTemplate.insert(project,"task_project");
    }
}
