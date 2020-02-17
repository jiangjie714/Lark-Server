package com.github.hollykunge.security.admin.vo;

import lombok.Data;

/**
 * 业务对象：前端获取用户信息
 * @author 协同设计小组
 * @date 2017/8/22
 */
@Data
public class FrontUser {
    /**
     * 系统内id
     */
    private String id;
    /**
     * 身份证号
     */
    private String PId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 组织编码
     */
    private String orgCode;
    /**
     * 自述信息
     */
    private String description;
    /**
     * 密级
     */
    private String secretLevel;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 状态0离职1在职
     */
    private Integer status;
    /**
     * 电话
     */
    private Integer mobile;
    /**
     * 0未删除 1删除
     */
    private Integer deleted;
    /**
     * 角色
     */
    private String roleId;
    /**
     * 岗位
     */
    private String positions;

    private UserRole userRole;

}
