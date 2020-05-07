package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.Date;

@Data
public class MegReadLogVo {
    //    id
    private String    id;
    //    发送人id，被阅读人
    private String   sender;
    //    发送人姓名
    private String   senderName;
    //    发送人身份证
    private String   senderSN;
    //    接收人
    private String   reviser;
    //    接收人姓名
    private String    reviserName;
    //    接收人身份证，当前登录人
    private String    reviserSN;
    //    阅读时间
    private Date readtime;
}

