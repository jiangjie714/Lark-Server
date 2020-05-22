package com.github.hollykunge.security.admin.dto.authority;

import lombok.Data;

/**
 * @description: 菜单功能权限
 * @author: dd
 * @since: 2019-06-01
 */
@Data
public class ActionEntitySet {

    /**
     * action->method
     * 功能add、query、get、update、delete
     */
    private String method;
    /**
     * describe->description
     * 描述：新增、查询、详情、修改、删除
     */
    private String description;
    /**
     * 默认选中
     */
    private Boolean defaultCheck;
    /**
     * 编码
     */
    private String code;
}
