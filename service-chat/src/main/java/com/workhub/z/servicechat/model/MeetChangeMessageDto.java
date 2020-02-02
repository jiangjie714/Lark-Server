package com.workhub.z.servicechat.model;

import lombok.Data;

import java.util.List;

/**
*@Description: 会议通过socket变更信息交互
*@Author: 忠
*@date: 2019/10/14
*/
@Data
public class MeetChangeMessageDto {
//    会议id
    private String meetId;
//    会议议程
    private String meetingProgress;
//    允许发言
    private String allowSpeech;
//    会议角色变更
    private List<UserMeetRole> userMeetRoleList;

}
