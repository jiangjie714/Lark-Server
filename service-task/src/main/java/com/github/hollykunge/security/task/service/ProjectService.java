package com.github.hollykunge.security.task.service;


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
   // void createProject(Project project);

    /**
     * 删除指定项目
     * @param projectCode
     */
    void delete(String projectCode);

    /**
     * 修改项目信息
     * @param data
     */
    //void edit(Project data);
}

