package com.workhub.z.servicechat.model;

import java.util.Date;

/**
*@Description: 联系人消息
*@Author: 忠
*@date: 2019/6/23
*/
public class ContactsMessageDto {
    private String content;
    private Date createtime;
    private String contactsId;
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getContactsId() {
        return contactsId;
    }

    public void setContactsId(String contactsId) {
        this.contactsId = contactsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
