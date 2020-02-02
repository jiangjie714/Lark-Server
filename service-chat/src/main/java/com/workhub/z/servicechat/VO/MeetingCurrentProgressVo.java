package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description: 会议当前议程vo
 * date:2019/10/22 11:02
 **/
@Data
public class MeetingCurrentProgressVo extends GeneralCodeNameVo{
    /**会议议程下标*/
    private String index;
}
