package com.github.hollykunge.security.admin.vo;

import com.github.hollykunge.security.common.vo.TreeNode;
import lombok.Data;


/**
 * @author zhhongyu
 * 组织用户实体类（展示组织树和子节点用户）
 */
@Data
public class OrgUser extends TreeNode {
    private String key;

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
