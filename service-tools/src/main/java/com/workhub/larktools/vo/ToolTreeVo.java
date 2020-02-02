package com.workhub.larktools.vo;

import java.io.Serializable;

/**
 * author:admin
 * description:
 * date:2019/8/14 10:19
 **/
public class ToolTreeVo implements Serializable {
    private static final long serialVersionUID = 7857453400185009151L;
    private String id;
    private String nodeName;
    private String pid;//父节点，-1表示根节点
    private String createTime;
    private String orderBy;
    private String level;//层 从1开始
    private String isLeaf;//是否叶子节点
    private String treeType;//树类型
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }
}
