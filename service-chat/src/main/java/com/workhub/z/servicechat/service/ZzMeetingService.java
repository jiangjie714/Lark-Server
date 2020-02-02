package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.MeetingVo;
import com.workhub.z.servicechat.entity.ZzMeeting;
import com.workhub.z.servicechat.model.MeetingDto;

import java.util.Map;

/**
 * @author:zhuqz
 * description:会议服务
 * date:2019/9/20 10:58
 **/
public interface ZzMeetingService {
    /** 新增*/
    String add(ZzMeeting zzMeeting);
    /**查询单条会议信息*/
    MeetingVo queryById(String meetId);
    /**修改*/
    int update(ZzMeeting zzMeeting);
    /**删除*/
    int delete(String meetId);
    /**创建会议*/
    ObjectRestResponse createMeeting(String meetingJson);
    /**查询议程代码表*/
    ListRestResponse getMeetProgressCodeList();
    /**会议角色代码表*/
    ListRestResponse getMeetRoleCodeList();
    /**会议功能代码表*/
    ListRestResponse getMeetFunctionCodeList();
    /**会议议程变更*/
    ObjectRestResponse changeMeetAgenda(Map param);

    /**
     * 更改会议议程列表
     * @param zzMeeting
     * @return
     */
    int changeMeetAgendaList(ZzMeeting zzMeeting);
    /**获取会议列表for联系人*/
    ListRestResponse getMeetingListForContacts(String userId);

    /**
//     *
     * 会议变更推送前端
     * @param userId 人员
     * @param meetingId 会议id
     * @return
     */
    ObjectRestResponse pushNewMeetInfToSocket(String userId,String meetingId);

    /**
     * 获取会议信息，不包括人员，供内部接口调用
     * @param meetId
     * @return
     */
     MeetingDto getMeetInf(String meetId);
}
