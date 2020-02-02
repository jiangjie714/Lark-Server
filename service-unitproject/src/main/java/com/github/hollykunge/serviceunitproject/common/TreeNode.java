package com.github.hollykunge.serviceunitproject.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 协同设计小组 on 2017/6/12.
 */
public class TreeNode implements Serializable {
    protected String id;
    protected String parentId;

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    List<TreeNode> children = new ArrayList<TreeNode>();

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

    public void add(TreeNode node) {
        children.add(node);
    }
}
