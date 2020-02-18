package com.github.hollykunge.security.admin.api.dto;

import lombok.Data;


/**
 * <p>admin服务用user，不使用frontUser</p>
 * <p>frontUser为提供远程调用的user实体</p>
 * @author zhhongyu
 */
@Data
public class AdminUser {
    /**
     * 系统内id
     */
    private String id;
    /**
     * 身份证号 username->pId
     */
    private String PId;
    /**
     * 用户姓名
     */
    private String name;

    private String password;
    /**
     * 自述信息
     */
    private String description;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 状态0离职1在职
     */
    private Integer status;
    /**
     * 电话 telephone->mobile
     */
    private Integer mobile;
    /**
     * 系统内删除状态0未删除 暂无
     */
    private Integer deleted;
    /**
     * 人员排序
     */
    private Long orderId;
    /**
     * 角色
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 用户组织id
     */
    private String orgId;
    /**
     *密级
     */
    private String secretLevel;

    private String orgCode;

    private String orgName;

  private String pathName;

    private String pathCode;

}
