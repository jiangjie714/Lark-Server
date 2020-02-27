package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.UserCurrentDayMeetJobVo;
import com.workhub.z.servicechat.entity.meeting.ZzMeetingUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议用户管理
 * date:2019/9/20 14:20
 **/
public interface ZzMeetingUserDao extends Mapper<ZzMeetingUser> {
    int addUser(@Param("params") ZzMeetingUser zzMeetingUser);
    int delUser(@Param("meetId") String meetId,@Param("userId") String userId);
    List<Map> getMeetAllUsers (@Param("meetId") String meetId);
    List<String> getMeetingByUserId (@Param("userId") String userId);
    int updateUser(@Param("params") ZzMeetingUser zzMeetingUser);
    int addListUsers(@Param("users") List<ZzMeetingUser> users);

    /**
     * 批量删除用户
     * @param users
     * @return
     */
    int delListUsers(@Param("users") List<ZzMeetingUser> users);
    int delMeetAllUser(@Param("meetId") String meetId);
    /**
     * 更新用户列表
     *
     * @param meetingUsers
     * @return: int
     * @Author: zhuqz
     * @Date: 2019/10/21
    **/
    int updateUserList(@Param("users")List<ZzMeetingUser> meetingUsers);

    /**
     * 获取用户当前需提醒的会议列表
     * @param userId
     * @return
     */
    List<UserCurrentDayMeetJobVo> getUserCurrentDayMeetJob(@Param("userId") String userId);
}
