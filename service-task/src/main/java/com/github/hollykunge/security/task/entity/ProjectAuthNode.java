package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecated task_project_auth_node 项目角色与权限
 */
@Data
@Document(collection="task_project_auth_node")
public class ProjectAuthNode {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;

    /**
     * 角色 id
     */
    @Field("auth")
    private String auth;
    /**
     * 节点路径
     */
    @Field("node")
    private String node;
}
