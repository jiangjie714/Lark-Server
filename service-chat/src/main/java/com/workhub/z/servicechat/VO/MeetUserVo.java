package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @author:zhuqz
 * description:会议用户
 * date:2019/9/20 14:27
 **/
@Data
public class MeetUserVo {
    /**用户id*/
    private String userId;
    /**用户名称*/
    private String userName;
    /**用户身份证*/
    private String userNo;
    /**用户组织id*/
    private String userOrgCode;
    /**用户组织名称*/
    private String userOrgName;
    /**用户组织全名称*/
    private String pathName;
    /**头像*/
    private String userImg;
    /**密级*/
    private String userLevel;
    /**角色编码*/
    private String userRoleCode;
    /**角色名称*/
    private String userRoleName;
    /**参加会议时间*/
    private String joinTime;
    /**能否发言 1是 0否*/
    private String canMsg;
    /**是否在线 1是 0否*/
    private String online;
    /**用户功能列表*/
    private List<GeneralCodeNameVo> functionList;
}
