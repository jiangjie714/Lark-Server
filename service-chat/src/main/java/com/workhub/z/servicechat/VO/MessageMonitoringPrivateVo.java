package com.workhub.z.servicechat.VO;

/**
 * author:zhuqz
 * description:消息监控私聊
 * date:2019/8/14 16:24
 **/
public class MessageMonitoringPrivateVo {
    //id
    private String id;
    //发送人姓名
    private String senderName;
    //操作者身份证号
    private String senderSn;
    //操作对象id
    private String senderId;
    //发送人密级
    private String senderLevel;
    //发送时间 yyyy-mm-dd hh:mi:ss
    private String sendTime;
    //接收人姓名
    private String receiverName;
    //接收方id
    private String receiverId;
    //ip
    private String ip;
    //状态
    private String status;
    //消息密级
    private String messageLevel;
    //消息内容
    private String messageContent;

    public String getSenderSn() {
        return senderSn;
    }

    public void setSenderSn(String senderSn) {
        this.senderSn = senderSn;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
