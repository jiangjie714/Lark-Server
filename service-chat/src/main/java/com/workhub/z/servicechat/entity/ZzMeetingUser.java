package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhuqz
 * description:会议人员表实体类
 * date:2019/9/20 10:12
 **/
@Data
public class ZzMeetingUser implements Serializable {

    private static final long serialVersionUID = 6328681281803033717L;
    /**主键*/
    private String id  ;
    /**会议id*/
    private String meetingId  ;
    /**成员id*/
    private String userId  ;
    /**成员名称*/
    private String userName  ;
    /**成员身份证*/
    private String userNo  ;
    /**成员角色*/
    private String roleCode  ;
    /**用户密级*/
    private String userLevel;
    /**创建时间*/
    private Date crtTime;
    /**创建人id*/
    private String crtUser  ;
    /**创建人名称*/
    private String crtName ;
    /**创建人ip*/
    private String crtHost;
    /**创建人身份证*/
    private String crtNo  ;
    /**更新时间*/
    private Date updTime;
    /**更新人id*/
    private String updUser  ;
    /**更新人名称*/
    private String updName ;
    /**更新人ip*/
    private String updHost;
    /**更新人身份证*/
    private String updNo  ;
    /**当前是否可以发言1是0否*/
    private String canMsg;
}
