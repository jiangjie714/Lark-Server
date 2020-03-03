package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * @author fansq
 * @deprecation task_project_version 项目版本
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_version")
public class ProjectVersion {
    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     *编号
     */
    @Field("code")
    private String code;
    /**
     *版本库名称
     */
    @Field("name")
    private String name;
    /**
     *描述
     */
    @Field("description")
    private String description;
    /**
     *创建时间
     */
    @Field("create_time")
    private String createTime;
    /**
     *类型名称
     */
    @Field("update_time")
    private String updateTime;
    /**
     *组织id
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     *发布时间
     */
    @Field("publish_time")
    private String publishTime;
    /**
     *开始时间
     */
    @Field("start_time")
    private String startTime;
    /**
     *状态。0：未开始，1：进行中，2：延期发布，3：已发布
     */
    @Field("status")
    private String status;
    /**
     *进度百分比
     * todo 暂定double
     */
    @Field("schedule")
    private Double schedule;
    /**
     *预计发布时间
     */
    @Field("plan_publish_time")
    private String planPublishTime;
    /**
     *版本库编号
     */
    @Field("features_code")
    private String featuresCode;

}
