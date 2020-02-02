package com.github.hollykunge.serviceunitproject.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author guxq
 * 项目单元实体类（展示项目单元和团队成员）
 */
@Data
public class ProjectUnitUser extends TreeNode implements Serializable {

    private static final long serialVersionUID = -3215943548965154778L;

   // private String name;

    private String orgUserId;

    private Long userOrder;

    private String partaskOrgUserId;

    private String ddOrgUserId;

    private String ddPartaskUserId;

    private String superior;

    private String subordinate;

    private String relatedUserId;

    //private String icon;
    /**
     * 如果为组织orgNode，如果为用户userNode
     */
  //  private String scopedSlotsTitle;
    /**
     * 是否在线，默认为true
     */
    private Boolean online;


}