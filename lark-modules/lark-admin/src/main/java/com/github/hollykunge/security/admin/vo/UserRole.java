package com.github.hollykunge.security.admin.vo;

import com.github.hollykunge.security.admin.dto.authority.FrontPermission;
import lombok.Data;

import java.util.List;

/**
 * 业务对象：用户角色权限，负责前端菜单展示使用
 *
 * @author: holly
 * @since: 2019/4/29
 */
@Data
public class UserRole {
    /**
     * 角色id
     */
    private String id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * describe->description
     * 角色描述
     */
    private String description;
    /**
     *
     * 角色状态
     */
    private Integer status;
    /**
     * creatorId->crtUser
     * 创建人id
     */
    private String crtUser;
    /**
     * createTime->crtTime
     * 创建时间
     */
    private Long crtTime;
    /**
     *
     * 删除状态0未删除
     */
    private Integer deleted;

    private List<FrontPermission> frontPermissionList;
}
