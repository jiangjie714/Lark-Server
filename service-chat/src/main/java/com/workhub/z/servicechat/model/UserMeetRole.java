package com.workhub.z.servicechat.model;

import lombok.Data;

/**
*@Description: 会议人员角色
*@Author: 忠
*@date: 2019/10/14
*/
@Data
public class UserMeetRole {
    private String userId;//人id
    private String userLevels;//人等级
    private String img;//头像
    private String roleCode;//角色码
}
