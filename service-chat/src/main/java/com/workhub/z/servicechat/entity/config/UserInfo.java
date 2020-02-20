package com.workhub.z.servicechat.entity.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rpc对外获取用户信息接口使用
 *
 * @author 协同设计小组
 * @create 2017-06-21 8:12
 */
@Data
public class UserInfo implements Serializable{
    private String id;
    private String PId;
    private String name;
    private String password;
    private String description;
    private String orgCode;
    private String orgName;
    private Integer demo;
    /**
     * 父级组织名称
     */
    private String parentOrgName;
    /**
     * 密级 40-fm,60-mm,80-jm
     */
    private String secretLevel;
    /**
     * 性别 1男 2女 3未知
     */
    private String gender;
    /**
     * 排序
     */
    private Long orderId;
    /**
     * 出入证号
     */
    private String empCode;
    /**
     * 出生年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date birthDate;
    /**
     * 办公电话
     */
    private String oTel;
    /**
     * 办公邮件
     */
    private String oEmail;
    /**
     * 行政岗位
     */
    private String workPost;
    /**
     * 技术岗位
     */
    private String tecPost;
    /**
     * 是否删除，0否，1是
     */
    private String deleted;
    /**
     * 姓
     */
    private String refa;
    /**
     * 名
     */
    private String refb;
    /**
     * 头像
     */
    private String avatar;


    /**
     * 岗位名称
     */
    private String posName;
    /**
     * 岗位描述
     */
    private String posDec;
    /**
     * 岗位类型
     */
    private String posType;
    /**
     * 岗位权限
     */
    private Integer posPermission;


    //以下为角色
    private String roleCode;

    private String roleName;

    private String rolePath;

    private String roleType;
    /**
     * org层级全路径
     */
    private String pathName;

}
