package com.github.hollykunge.security.task.service;


import com.github.hollykunge.security.task.entity.Project;

/**
 * @author fansq
 * @since 20-3-7
 * @deprecation 项目service
 */
public interface ProjectService {

    /**
     * 新建一个项目
     * @param project
     */
    void createProject(Project project);
}

