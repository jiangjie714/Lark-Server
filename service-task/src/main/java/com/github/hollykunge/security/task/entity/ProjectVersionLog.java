package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * @author fansq
 * @deprecation task_project_version_log 项目版本日志
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_version_log")
public class ProjectVersionLog {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     *code
     */
    @Field("code")
    private String code;
    /**
     *操作人id
     */
    @Field("member_code")
    private String memberCode;
    /**
     *操作内容
     */
    @Field("content")
    private String content;
    /**
     *日志描述
     */
    @Field("remark")
    private String remark;
    /**
     *操作类型
     */
    @Field("type")
    private String type;
    /**
     *添加时间
     */
    @Field("create_time")
    private String createTime;
    /**
     *任务id
     */
    @Field("source_code")
    private String sourceCode;
    /**
     *项目编号
     */
    @Field("project_code")
    private String projectCode;
    /**
     *icon
     */
    @Field("icon")
    private String icon;
    /**
     *版本库编号
     */
    @Field("features_code")
    private String featuresCode;
}
