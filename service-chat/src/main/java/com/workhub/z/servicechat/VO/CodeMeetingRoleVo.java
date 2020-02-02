package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @author:zhuqz
 * description:会议角色代码
 * date:2019/9/23 14:50
 **/
@Data
public class CodeMeetingRoleVo {
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
    /**角色有哪些功能*/
    private List<CodeMeetingFunctionVo> meetFunction;
}
