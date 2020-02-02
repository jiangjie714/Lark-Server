package com.github.hollykunge.security.common.vo;

import java.io.Serializable;
import java.util.*;

/**
 * Created by 协同设计小组 on 2017/6/12.
 */
public class TreeNode implements Serializable {
    protected String id;
    protected String parentId;

    public Set<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Set<TreeNode> children) {
        this.children = children;
    }

    Set<TreeNode> children = new TreeSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void add(TreeNode node){
        children.add(node);
    }
}
