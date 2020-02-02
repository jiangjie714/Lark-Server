package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 消息存储(ZzMessageInfo)实体类
 *
 * @author makejava
 * @since 2019-06-23 14:40:58
 */
@Data
public class ZzMessageInfo implements Serializable {
    private static final long serialVersionUID = -38086025213877508L;
    
    private String msgId;
    //当前用户
    private String sender;
    //联系人
    private String receiver;
    //产生时间
    private Date createtime;
    //消息密级
    private String levels;
    //消息内容
    private String content;
    //群组（greoup）,私聊（user）
    private String type;
    //是否跨越场所1是0否
    private String iscross;
    //IP
    private String ip;
    //sn
    private String senderSN;
    //发送人组织编码
    private String orgCode;
    //发送人组织名称
    private String orgName;
}