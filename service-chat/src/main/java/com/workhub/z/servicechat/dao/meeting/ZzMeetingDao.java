package com.workhub.z.servicechat.dao.meeting;

import com.workhub.z.servicechat.VO.GeneralCodeNameVo;
import com.workhub.z.servicechat.entity.meeting.ZzMeeting;
import com.workhub.z.servicechat.model.MeetingDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议
 * date:2019/9/20 10:37
 **/
public interface ZzMeetingDao  extends Mapper<ZzMeeting> {
    int add(@Param("params") ZzMeeting zzMeeting);
    MeetingDto queryById(@Param("meetId") String meetId);
    List<MeetingDto> getMeetingListForContacts(@Param("userId") String userId);
    int update(@Param("params") ZzMeeting zzMeeting);
    int deleteData (@Param("meetId") String meetId);
    List<GeneralCodeNameVo> getMeetProgressCodeList();
    List<Map> getMeetRoleCodeList();
    List<GeneralCodeNameVo> getMeetFunctionCodeList();
    List<GeneralCodeNameVo> getMeetFunctionsByCodes(@Param("codes") String codes);
}
