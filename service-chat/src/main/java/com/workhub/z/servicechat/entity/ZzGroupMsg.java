package com.workhub.z.servicechat.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 群组消息表(ZzGroupMsg)实体类
 *
 * @author 忠
 * @since 2019-05-10 11:38:02
 */
public class ZzGroupMsg implements Serializable {
    private static final long serialVersionUID = -87069741670583916L;
    //消息id
    private String msgId;
    //消息发出者
    private String msgSender;
    //消息接收者
    private String msgReceiver;
    //消息发出时间
    private Date sendTime;
    //消息内容
    private String msg;
    //消息类型（text：文本；img：图片：emoji：表情；file：文件）
    private String msgType;
    //如果是图片或者文件，存储文件路径
    private String msgPath;
    //0
    private String isDelete;
    //0  --是否重要信息--,
    private String isImportant;
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

    public String getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(String isImportant) {
        this.isImportant = isImportant;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

}