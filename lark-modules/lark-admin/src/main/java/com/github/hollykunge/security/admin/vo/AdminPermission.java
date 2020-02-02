package com.github.hollykunge.security.admin.vo;

import lombok.Data;

import java.util.List;
/**
 * 角色所属功能（仅供admin服务使用）
 *
 * @author: zhhongyu
 * @since 2019/5/27
 */
@Data
public class AdminPermission {

        /**
         * 角色id
         */
        private String roleId;
        /**
         * permissionId->menuId
         */
        private String menuId;
        /**
         * permissionName->title
         */
        private String title;
        /**
         * actions->methods
         */
        private String methods;
        /**
         * 暂时没用上null
         */
        private String actionList;
        /**
         * 暂时没用上null
         */
        private String dataAccess;

        private List<AdminElement> actionEntitySetList;

        private String type;
}
