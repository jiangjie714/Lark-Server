package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecation task_project_auth_collection  项目-收藏
 * todo 其实我感觉可以直接存内置文档集合 省去这些关联表
 */
@Data
@Document(collection="task_project_auth_collection")
public class ProjectCollection {

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
     * 成员id
     */
    @Field("member_code")
    private String member_code;

    /**
     * 创建时间
     */
    @Field("create_time")
    private Date createTime;
}
