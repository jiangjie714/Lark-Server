package com.github.hollykunge.security.admin.api.authority;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fansq
 * @since 20-3-23
 * @deprecation 门户统计功能前端信息展示实体   文件量排行实体
 */
@Data
public class FileNum implements Serializable {
    /**
     * 单位名称  部门名称
     */
    private String x;
    /**
     *  文件量
     */
    private Long y;
}
