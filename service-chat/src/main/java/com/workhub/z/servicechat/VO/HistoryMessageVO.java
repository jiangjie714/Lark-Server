package com.workhub.z.servicechat.VO;

import java.util.List;

/**
*@Description: 历史消息结构体
*@Author: 忠
*@date: 2019/6/22
*/
public class HistoryMessageVo {
//      联系人
    private String contactsId;

    private List<String> content;

    public String getContactsId() {
        return contactsId;
    }

    public void setContactsId(String contactsId) {
        this.contactsId = contactsId;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        String[] toBeStored = content.toArray(new String[content.size()]);
        return "["+contactsId +",["+
                "contactsId='" + contactsId + '\'' +
                ", content=" + content +
                '}'
                ;
    }
    ////      消息体
//    private List<MessageVo> messageVO;
//
//    public String getContacts() {
//        return contactsId;
//    }
//
//    public void setContacts(String contactsId) {
//        contactsId = contactsId;
//    }
//
//    public List<MessageVo> getMessageVO() {
//        return messageVO;
//    }
//
//    public void setMessageVO(List<MessageVo> messageVO) {
//        this.messageVO = messageVO;
//    }
}
