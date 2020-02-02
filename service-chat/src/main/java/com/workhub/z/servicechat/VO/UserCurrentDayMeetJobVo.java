package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description: 用户当天会议定时提醒vo
 * date:2019/11/26 9:26
 **/
@Data
public class UserCurrentDayMeetJobVo {
    /**
     * 会议id
     */
    private String meetId;
    /**
     * 会议名称
     */
    private String meetName;
    /**
     * 会议开始时间 yyyy-MM-dd HH:mm:ss
     */
    private String startTime;
    /**
     * 会议结束时间 yyyy-MM-dd HH:mm:ss
     */
    private String endTime;
}
