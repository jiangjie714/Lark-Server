package com.workhub.z.servicechat.model;

import lombok.Data;

import java.util.Date;

/**
 * @author:zhuqz
 * description: 原始消息，未处理成json前
 * date:2019/12/4 14:13
 **/
@Data
public class RawMessageDto {
    private String msgId;//消息id
    private String type;//类型私人 群 会议
    private String   senderid;//发送人id
    private String  sendername;//发送人名称
    private String  senderavatar;//发送人头像
    private String  senderlevels;//发送密级
    private String senderpid;//发送人身份证
    private String  receiverid;//接收人id
    private String receivername;//接收人姓名
    private String  receiveravatar;//接收人头像
    private String  receivermembernum;//接收人如果是群体 群体有多少人
    private String  receiverlevels;//接收人密级
    private String  receivergroupowner;//接收人如果是群，群主id
    private String  receiverpid;
    private String   fileid;//如果是 附件id
    private String  fileext;//如果是附件 附件后缀
    private String   filesize;//如果是附件 附件大小
    private String  levels; //消息密级
    private String   frontid; //前端消息id
    private String  filetype; // 消息类型 1文字2图片3附件999会议变动
    private String  msg; //消息内容（附件名称）
    private String createtime;//发送时间
    private String cross;//消息跨场所属性
    private String ip;//发送消息的ip
    /**额外信息*/
    private String  contactsid; //最近联系人
}

