package com.github.hollykunge.security.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhhongyu
 * 组织用户实体类（展示组织树和子节点用户）
 */
@Data
public class OrgUser extends TreeNode implements Serializable {

    private static final long serialVersionUID = -3215943548965154778L;

    private String key;

    private Long order;

    private String title;

    private String icon;
    /**
     * 如果为组织orgNode，如果为用户userNode
     */
    private String scopedSlotsTitle;
    /**
     * 是否在线，默认为true
     */
    private Boolean online;



}