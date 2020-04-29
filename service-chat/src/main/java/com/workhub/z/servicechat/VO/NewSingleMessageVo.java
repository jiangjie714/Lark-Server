package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * author:zhuqz
 * description:
 * date:2019/12/9 10:51
 **/
@Data
public class NewSingleMessageVo extends NewMessageVo{
    /**   消息发送时间（当天显示时间，昨天以前显示期）*/
    private String sendTimeShort;
}
