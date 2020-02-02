package com.github.hollykunge.serviceunitproject.common;

import lombok.Data;


/**
 * <p>配合前台，减少前台工作量使用
 *
 * @author guxq
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
    private String pId;
    /**
     * 用户姓名
     */
    private String name;
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
     * 角色
     */
    private String roleId;
    /**
     * 用户组织id
     */
    private String orgId;
    /**
     * 密级
     */
    private String secretLevel;

    /**
     * 借用技术岗位展示总师技术负责人参与人
     */
    private String tecPost;

    /**
     * 技术岗位
     */
     private String workPost;;

//    private UserRole userRole;
}
