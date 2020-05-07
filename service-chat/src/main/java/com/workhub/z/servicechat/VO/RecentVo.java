package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/4/30 17:18
 * @description: 最近联系人
 */
@Data
public class RecentVo implements Serializable {
    private static final long serialVersionUID = -5340766502601818452L;
    /**联系人id*/
    private String contactId;
    /**联系人名字*/
    private String contactName;
    /**联系人头像*/
    private String contactAvartar;
    /**消息类型1文字2图片3附件*/
    private String msgType;
    /**消息内容*/
    private String msgContent;
    /**消息密级*/
    private String msgLevel;
    /**发送时间*/
    private String sendTime;
    /***/
    private String top;
    /**是否at1是0否*/
    private String at;
    /**是否撤销1是0否*/
    private String cancel;
    /**未读消息条数*/
    private String unreadNum;
}