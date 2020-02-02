package com.workhub.larktools.vo;

import java.io.Serializable;

/**
 * author:zhuqz
 * description:
 * date:2019/8/30 14:08
 **/
public class ToolFileAuthVo implements Serializable {
    private static final long serialVersionUID = 1577992696465991857L;
    private String id;
    private String fileId;
    private String createTime;
    private String creator;
    private String orgCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
