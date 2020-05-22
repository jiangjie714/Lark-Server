package com.github.hollykunge.security.admin.dto.authority;

import lombok.Data;

import java.util.List;

/**
 * @description: 权限业务实体
 * @author: dd
 * @since: 2019-06-01
 */
@Data
public class FrontPermission implements Comparable<FrontPermission> {
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 菜单id
     * permissionId->menuId
     */
    private String menuId;
    /**
     * 权限标识
     */
    private String permissionId;
    /**
     * 菜单名称
     * permissionName->title
     */
    private String title;
    /**
     * 方法列表
     * actions->methods
     */
    private String methods;
    /**
     * 权限资源路径
     */
    private String uri;

    private List<ActionEntitySet> actionEntitySetList;

    @Override
    public int compareTo(FrontPermission frontPermission) {
        Integer length = this.uri.length();
        return length.compareTo(frontPermission.getUri().length());
    }
}
