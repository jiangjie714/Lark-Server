package com.github.hollykunge.security.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

/**
 * @author: zhhongyu
 * @description: 给内部服务提供人员信息
 * @since: Create in 16:36 2019/9/19
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class RpcUserInfo {
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String pId;
    /**
     * 组织机构编码
     */
    private String orgCode;
    /**
     * 组织名称
     */
    private String orgName;
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

    //以下为岗位
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



}
