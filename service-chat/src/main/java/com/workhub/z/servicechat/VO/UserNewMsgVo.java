package com.workhub.z.servicechat.VO;

import java.util.Date;

public class UserNewMsgVo {

    private String msgId;//消息id
    private String tableType;//表来源类型
    private String msgSener;//消息人
    private String msgReceiver;//发送人
    private Date sendTime;//消息时间
    private String msg;//消息
    private String msgType;//消息类型
    private String levels;//消息等级

    public String getMsgReceiver() {
        return msgReceiver;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getMsgSener() {
        return msgSener;
    }

    public void setMsgSener(String msgSener) {
        this.msgSener = msgSener;
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

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }
}
