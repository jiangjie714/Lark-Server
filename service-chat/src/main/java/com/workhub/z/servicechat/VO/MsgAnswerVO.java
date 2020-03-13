package com.workhub.z.servicechat.VO;
/**
*@Description: 应答报文
*@Author: 忠
*@date: 2019/7/19
*/
public class MsgAnswerVO {
//      协议码 11
    private int code;
//      消息id
    private String nId;
//      toid
    private String contactId;
//      原id
    private String oId;
//      应答内容
    private String content;
//      应答状态 0 正常；1 异常
    private int status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
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
                "code:" + code +
                ", nId:'" + nId + '\'' +
                ", contactId:'" + contactId + '\'' +
                ", oId:'" + oId + '\'' +
                ", content:'" + content + '\'' +
                ", status:" + status +
                '}';
    }
}
