package com.workhub.z.servicechat.VO;

import java.io.Serializable;

/**
*@Description: 应答报文
*@Author: 忠
*@date: 2019/7/19
*/
public class MsgAnswerVo implements Serializable {
    private static final long serialVersionUID = -3286084993874070720L;
//      消息id
    private String nId;
//      toid
    private String contactId;
//      应答内容
    private String content;
//      应答状态 0 正常；1 异常
    private int status;

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                ", nId:'" + nId + '\'' +
                ", contactId:'" + contactId + '\'' +
                ", content:'" + content + '\'' +
                ", status:" + status +
                '}';
    }
}
