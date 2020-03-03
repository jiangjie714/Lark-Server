package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecation task_project 项目
 */
@Data
@Document(collection="task_project")
public class Project {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;

    /**
     * 封面
     */
    @Field("cover")
    private String cover;

    /**
     * 名称
     */
    @Field("name")
    private String name;
    /**
     *编码
     */
    @Field("code")
    private String code;
    /**
     *描述
     */
    @Field("description")
    private String description;
    /**
     *访问控制类型
     * 'open','private','custom'
     * 字段 access_control_type
     */
    @Field("access_control_type")
    private String accessControlType;
    /**
     *可以访问项目的权限组（白名单）
     * todo 内嵌文档直接保存权限组信息 list
     * 字段 white_list
     */
    @Field("white_list")
    private List<ProjectAuth> whiteList;
    /**
     *排序
     */
    @Field("order")
    private Long order;
    /**
     *删除标记
     */
    @Field("deleted")
    private String deleted;
    /**
     *项目类型
     * 字段 template_code
     */
    @Field("template_code")
    private String templateCode;
    /**
     *项目进度
     * todo 这个类型有待考究 java的double和mongodb的double 会有精度损失
     */
    @Field("schedule")
    private Double schedule;
    /**
     *创建时间
     * 字段 create_time
     */
    @Field("create_time")
    private Date createTime;
    /**
     *组织id
     * 字段 organization_code
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     *删除时间
     * 字段 deleted_time
     */
    @Field("deleted_time")
    private Date deletedTime;
    /**
     *是否私有  1是 0否
     * 字段 is_private
     */
    @Field("is_private")
    private String isPrivate;
    /**
     *项目前缀
     */
    @Field("prefix")
    private String prefix;
    /**
     *是否开启项目前缀 1是 0 否
     * 字段 open_prefix
     */
    @Field("open_prefix")
    private String openPrefix;
    /**
     *是否归档 1是 0否
     */
    @Field("archive")
    private String archive;
    /**
     *归档时间
     * 字段 archive_time
     */
    @Field("archive_time")
    private Date archiveTime;
    /**
     * 是否开启任务开始时间 1是 0否
     *字段 open_begin_time
     */
    @Field("open_begin_time")
    private String openBeginTime;
    /**
     *是否开启新任务隐私模式 1是 0否
     * 字段 open_task_private
     */
    @Field("open_task_private")
    private String openTaskPrivate;
    /**
     *看板风格
     */
    @Field("task_board_theme")
    private String taskBoardTheme;
    /**
     *项目开始日期
     */
    @Field("begin_time")
    private Date beginTime;
    /**
     *项目截至日期
     */
    @Field("end_time")
    private Date endTime;
    /**
     *字段更新项目进度
     */
    @Field("auto_update_schedule")
    private String autoUpdateSchedule;
}
