package com.github.hollykunge.security.admin.vo;

import lombok.Data;

/**
 * 菜单内部功能控制实体（仅供admin服务使用）
 *
 * @author: zhhongyu
 * @since: 2019/5/27
 */
@Data
public class AdminElement {
    /**
     * elementId
     * 保存修改的权限资源会用到
     */
    private String id;
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
