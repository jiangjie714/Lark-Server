package com.workhub.z.servicechat.entity;

import java.io.Serializable;

/**
 * 消息阅读状态关系表(ZzMsgReadRelation)实体类
 *
 * @author makejava
 * @since 2019-05-23 13:27:22
 */
public class ZzMsgReadRelation implements Serializable {
    private static final long serialVersionUID = 277996289744180607L;
    //主键
    private String id;
    //发送人id
    private String sender;
    //接收人id
    private String receiver;
    //消息来源类型
    private String sendType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

}