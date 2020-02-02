package com.workhub.z.servicechat.entity;

import java.io.Serializable;
import java.util.Date;

public class ZzUploadFile implements Serializable {

    private static final long serialVersionUID = 2612762015028225960L;
    //主键
    private String id;
    //用户id
    private String userId;
    //用户名称
    private String userName;
    //上传时间
    private Date uploadTime;
    //是否成功
    private String successFlg;
    //是否群主文件
    private String isGroup;
    //接收人
    private String receiver;
    //接收人姓名
    private String receiverName;
    //文件id
    private String fileId;
    //文件密级
    private String levels;
    //是否审批通过
    private String approveFlg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getSuccessFlg() {
        return successFlg;
    }

    public void setSuccessFlg(String successFlg) {
        this.successFlg = successFlg;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getApproveFlg() {
        return approveFlg;
    }

    public void setApproveFlg(String approveFlg) {
        this.approveFlg = approveFlg;
    }
}
