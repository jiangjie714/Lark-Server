package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * @author fansq
 * @deprecation task_project_Node 项目端节点
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_node")
public class ProjectNode {
    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     *节点代码
     */
    @Field("node")
    private String node;
    /**
     *节点标题
     */
    @Field("title")
    private String title;
    /**
     *是否可设置为菜单
     */
    @Field("is_menu")
    private String isMenu;
    /**
     *是否启动RBAC权限控制
     */
    @Field("is_auth")
    private String isAuth;
    /**
     *是否启动登录控制
     */
    @Field("is_login")
    private String isLogin;

    /**
     *创建时间
     */
    @Field("create_at")
    private String createAt;
}
