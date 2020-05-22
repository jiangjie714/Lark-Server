package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.workhub.z.servicechat.VO.MeetUserVo;
import com.workhub.z.servicechat.VO.MeetingVo;
import com.workhub.z.servicechat.VO.UserCurrentDayMeetJobVo;
import com.workhub.z.servicechat.entity.meeting.ZzMeetingUser;

import java.util.List;

/**
 * @author:zhuqz
 * description:会议用户管理
 * date:2019/9/20 15:00
 **/
public interface ZzMeetingUserService {
    int addUser(ZzMeetingUser zzMeetingUser);
    int delUser(String meetId,String userId);
    int delMeetAllUser(String meetId);
    ListRestResponse queryMeetAllUsers(String meetId);
    List<MeetUserVo> queryMeetAllUsersVo(String meetId);
    int updateUser(ZzMeetingUser meetingUser);
    /**
    * @MethodName: updateUserList
     * 更新用户列表
     * @param: meetingUsers 用户列表
     * @return: int
     * @Author: zhuqz
     * @Date: 2019/10/21
    **/
    int updateUserList(List<ZzMeetingUser> meetingUsers);
    int addListUsers(List<ZzMeetingUser> users);
    List<String> getMeetingByUserId(String userId);

    /**
     * 编辑会议人员
     * @param meetingVo
     * @param userId 操作人id
     * @param userName 操作人姓名
     * @param userNo 操作人身份证
     * @param userIp 操作人ip
     * @return 1成功 -1失败 0成员过多
     */
    int editMeetUser(MeetingVo meetingVo, String userId, String userName, String userNo, String userIp);

    /**
     * 获取用户当天需要提醒会议列表
     * @param userId
     * @return
     */
    List<UserCurrentDayMeetJobVo> getUserCurrentDayMeetJob(String userId);

    /**
     * 查询用户未结束的会议id列表
     * @param userId
     * @return
     */
    List<String> listUserStartingMeetIds(String userId);
}
