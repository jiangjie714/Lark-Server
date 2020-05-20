package com.github.hollykunge.security.task.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fansq
 * @since 20-5-7
 * @deprecation 用于前端获取权限资源使用
 */
@Data
public class LarkTaskElement implements Serializable {
    /**
     * aet.id
     */
    private String elementTaskId;
    /**
     * aet.code
     */
    private String elementTaskCode;
    /**
     * aet.menu_id
     */
    private String menuId;
    /**
     * aet.method
     */
    private String method;
    /**
     * aet.name ,
     */
    private String elementTaskName;
    /**
     * aet.parent_id,
     */
    private String parentId;
    /**
     * aet.path,
     */
    private String path;
    /**
     * aet.type
     */
    private String elementTaskType;
    /**
     * aet.uri,
     */
    private String uri;
}
