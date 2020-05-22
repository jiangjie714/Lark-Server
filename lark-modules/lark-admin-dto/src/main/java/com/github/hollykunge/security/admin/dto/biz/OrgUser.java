package com.github.hollykunge.security.admin.dto.biz;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhhongyu
 * 组织用户实体类（展示组织树和子节点用户）
 */
@Data
public class OrgUser implements Serializable {

    private static final long serialVersionUID = -3215943548965154778L;

    private String id;

    /**
     * 如果为组织orgNode，如果为用户userNode
     */
    private String scopedSlotsTitle;
    private Long orderId;
    private String deleted;
    /**
     * 是否在线，默认为true
     */
    private Boolean online;
    /**
     * 组织相关
     */
    private String orgName;

    private String orgCode;
    /**
     * 人员相关
     */
    private String name;

    private String PId;

    private String avatar;
    /**
     * 增加长的组织
     */
    private String pathName;

}
