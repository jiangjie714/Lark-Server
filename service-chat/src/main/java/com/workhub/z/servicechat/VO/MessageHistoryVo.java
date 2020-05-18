package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author: zhuqz
 * @date: 2020/5/18 13:38
 * @description: 历史消息
 */
@Data
public class MessageHistoryVo {
    private String msgId;
    private String type;
    private String   senderId;
    private String  senderName;
    private String  senderAvatar;
    private String  senderLevels;
    private String senderPid;
    private String  receiverId;
    private String receiverName;
    private String  receiverAvatar;
    private String  receiverMemberNum;
    private String  receiverLevels;
    private String  receiverGroupOwner;
    private String  receiverPid;
    private String   fileId;
    private String  fileExt;
    private String   fileSize;
    private String  levels;
    private String   frontId;
    private String  fileType;
    private String  msg;
    private String createTime;
    private String  contactsId;
}