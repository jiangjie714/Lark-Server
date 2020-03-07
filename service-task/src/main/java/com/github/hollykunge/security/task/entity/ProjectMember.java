package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author fansq
 * @deprecation task_project_member 项目-成员
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_member")
public class ProjectMember extends BaseEntity{

    /**
     * 用户id
     */
    @Field("user_id")
    private String userId;

     /**
     * 加入时间
     */
    @Field("join_time")
    private String joinTime;

     /**
     * 角色
     */
    @Field("authorize")
    private ProjectRole authorize;

    /**
     * 姓名
     */
    @Field("NAME")
    private String name;
    /**
     * 身份证号
     */
    @Field("P_ID")
    private String PId;
    /**
     * 组织机构编码
     */
    @Field("ORG_CODE")
    private String orgCode;
    /**
     * 组织名称
     */
    @Field("ORG_NAME")
    private String orgName;
    /**
     * 密级 40-fm,60-mm,80-jm
     */
    @Field("SECRET_LEVEL")
    private String secretLevel;
    /**
     * 性别 1男 2女 3未知
     */
    @Field("GENDER")
    private String gender;
    /**
     * 排序
     */
    @Field("ORDER_ID")
    private Long orderId;
    /**
     * 出入证号
     */
    @Field("EMP_CODE")
    private String empCode;
    /**
     * 出生年月
     */
    @Field("BIRTH_DATE")
    private Date birthDate;
    /**
     * 办公电话
     */
    @Field("O_TEL")
    private String oTel;
    /**
     * 办公邮件
     */
    @Field("O_EMAIL")
    private String oEmail;
    /**
     * 行政岗位
     */
    @Field("WORK_POST")
    private String workPost;
    /**
     * 技术岗位
     */
    @Field("TEC_POST")
    private String tecPost;
    /**
     * 是否删除，0否，1是
     */
    @Field("DELETED")
    private String deleted;
    /**
     * 头像
     */
    @Field("AVATAR")
    private String avatar;
    /**
     * 描述
     */
    @Field("DESCRIPTION")
    private String description;
}
