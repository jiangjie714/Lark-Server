package com.github.hollykunge.security.admin.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "ADMIN_USER")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1235943548965154778L;
    /**
     * 姓名
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 身份证号
     */
    @Column(name = "P_ID")
    private String pId;
    /**
     * 组织机构编码
     */
    @Column(name = "ORG_CODE")
    private String orgCode;
    /**
     * 组织名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;
    /**
     * 密级 40-fm,60-mm,80-jm
     */
    @Column(name = "SECRET_LEVEL")
    private String secretLevel;
    /**
     * 性别 1男 2女 3未知
     */
    @Column(name = "GENDER")
    private String gender;
    /**
     * 排序
     */
    @Column(name = "ORDER_ID")
    private Long orderId;
    /**
     * 出入证号
     */
    @Column(name = "EMP_CODE")
    private String empCode;
    /**
     * 出生年月
     */
    @Column(name = "BIRTH_DATE")
    private Date birthDate;
    /**
     * 办公电话
     */
    @Column(name = "O_TEL")
    private String oTel;
    /**
     * 办公邮件
     */
    @Column(name = "O_EMAIL")
    private String oEmail;
    /**
     * 行政岗位
     */
    @Column(name = "WORK_POST")
    private String workPost;
    /**
     * 技术岗位
     */
    @Column(name = "TEC_POST")
    private String tecPost;
    /**
     * 是否删除，0否，1是
     */
    @Column(name = "DELETED")
    private String deleted;
    /**
     * 姓
     */
    @Column(name = "REFA")
    private String refa;
    /**
     * 名
     */
    @Column(name = "REFB")
    private String refb;
    /**
     * 头像
     */
    @Column(name = "AVATAR")
    private String avatar;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;


}