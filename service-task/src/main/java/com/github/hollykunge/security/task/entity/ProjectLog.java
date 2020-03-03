package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @deprecation task_project_log 项目日志
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_log")
public class ProjectLog {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     * code
     */
    @Field("code")
    private String code;
    /**
     * 操作人id
     */
    @Field("member_code")
    private String memberCode;
    /**
     * 操作内容
     */
    @Field("content")
    private String content;
    /**
     * remark
     */
    @Field("remark")
    private String remark;
    /**
     * 操作类型
     */
    @Field("type")
    private String type;
    /**
     * 添加时间
     */
    @Field("create_time")
    private Date createTime;
    /**
     * 任务id
     */
    @Field("source_code")
    private String sourceCode;
    /**
     * 场景类型
     */
    @Field("action_type")
    private String actionType;
    /**
     * to_member_code
     */
    @Field("to_member_code")
    private String to_member_code;
    /**
     * code 1是 0否
     */
    @Field("is_comment")
    private String isComment;
    /**
     * code
     */
    @Field("project_code")
    private String projectCode;
    /**
     * 图标
     */
    @Field("icon")
    private String icon;
    /**
     * 是否机器人 1是 0否
     */
    @Field("is_robot")
    private String isRobot;
}
