package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * @author fansq
 * @deprecation task_project_member 项目-成员
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_member")
public class ProjectMember {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     * 项目id
     */
    @Field("project_code")
    private String projectCode;
    /**
     * 项目id
     */
    @Field("project_code")
    private String memberCode;
     /**
     * 加入时间
     */
    @Field("join_time")
    private String joinTime;
    /**
     * 拥有者
     */
    @Field("isOwner")
    private Integer isOwner;
     /**
     * 角色
     */
    @Field("authorize")
    private String authorize;
}
