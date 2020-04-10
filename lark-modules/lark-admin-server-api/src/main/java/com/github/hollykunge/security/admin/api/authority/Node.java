package com.github.hollykunge.security.admin.api.authority;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fansq
 * @since 20-3-30
 * @deprecation 节点
 * 临时使用
 */
@Data
public class Node implements Serializable {

    /**
     * 使用orgcode
     */
    private String id;
    /**
     * 使用organme
     */
    private String name;
    /**
     * 颜色
     */
    private ItemStyle itemStyle;
    /**
     * 点的大小
     */
    private Double symbolSize;
    private String parnetId;
    private Integer level;
    private Attributes attributes;
}
