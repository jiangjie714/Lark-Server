package com.workhub.z.servicechat.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 私人消息(ZzPrivateMsg)实体类
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
public class ZzPrivateMsg implements Serializable {
    private static final long serialVersionUID = 249121391331731157L;
    //消息id
    private String msgId;
    //消息发出者
    private String msgSender;
    //消息接收者
    private String msgReceiver;
    //消息发出时间
    private Date sendTime;
    //消息接收时间
    private Date receiverTime;
    //消息是否已读
    private String isRead;
    //消息内容
    private String msg;
    //消息类型（text：文本；img：图片：emoji：表情；file：文件）
    private String msgType;
    //如果是图片或者文件，存储文件路径
    private String msgPath;
    //是否删除--,
    private String isDelete;
    //密级
    private String levels;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgReceiver() {
        return msgReceiver;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(Date receiverTime) {
        this.receiverTime = receiverTime;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgPath() {
        return msgPath;
    }

    public void setMsgPath(String msgPath) {
        this.msgPath = msgPath;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

}