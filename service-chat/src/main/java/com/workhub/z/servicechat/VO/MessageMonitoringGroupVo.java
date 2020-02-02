package com.workhub.z.servicechat.VO;

/**
 * author:admin
 * description:
 * date:2019/8/14 16:30
 **/
public class MessageMonitoringGroupVo {
    //id
    private String id;
    //发送人姓名
    private String senderName;
    //发送人密级
    private String senderLevel;
    //发送时间 yyyy-mm-dd hh:mi:ss
    private String sendTime;
    //群名称
    private String receiverName;
    //消息密级
    private String messageLevel;
    //消息内容
    private String messageContent;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderLevel() {
        return senderLevel;
    }

    public void setSenderLevel(String senderLevel) {
        this.senderLevel = senderLevel;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(String messageLevel) {
        this.messageLevel = messageLevel;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
