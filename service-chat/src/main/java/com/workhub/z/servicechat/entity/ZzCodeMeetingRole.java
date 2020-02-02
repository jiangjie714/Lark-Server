package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhuqz
 * description:会议角色编码表
 * date:2019/9/20 10:34
 **/
@Data
public class ZzCodeMeetingRole implements Serializable {

    private static final long serialVersionUID = -284710685836383771L;
    /**主键*/
    private  String id        ;
    /**编码*/
    private  String code        ;
    /**名称*/
    private  String name        ;
    /**创建时间*/
    private  String crtTime;
    /**创建人*/
    private  String crtUser;
    /**创建人姓名*/
    private  String crtName;
    /**创建人ip*/
    private  String crtHost;
    /**更新时间*/
    private  String updTime;
    /**更新人*/
    private  String updUser;
    /**更新人姓名*/
    private  String updName;
    /**更新人ip*/
    private  String updHost;
    /**是否使用中1是0否*/
    private  String isUse      ;
    /**角色有哪些功能，对应代码，逗号分割*/
    private  String meetFunction;
}
