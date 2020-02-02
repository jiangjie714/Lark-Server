package com.workhub.z.servicechat.entity;

import java.io.Serializable;

/**
 * 标记消息关系表(ZzMsgTabRelation)实体类
 *
 * @author makejava
 * @since 2019-05-23 16:12:40
 */
public class ZzMsgTabRelation implements Serializable {
    private static final long serialVersionUID = -77185750080438580L;
    //主键
    private String id;
    //标记id
    private String tabId;
    //上传文件id
    private String fileId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

}