package com.github.hollykunge.security.admin.api.authority;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fansq
 * @since 20-3-30
 * @deprecation 连接线
 * 临时使用
 */
@Data
public class Link implements Serializable {
    /**
     * 暂时自增
     */
    private Integer id;

    private String name=null;
    /**
     * 两点 点orgcode
     */
    private String source;
    /**
     * 两点 点orgcode
     */
    private String target;
    private LineStyle lineStyle;
}
