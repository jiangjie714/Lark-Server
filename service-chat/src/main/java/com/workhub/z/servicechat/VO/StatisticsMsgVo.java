package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:08
 * @description: 消息统计
 */
@Data
public class StatisticsMsgVo {
    private String msgCount;//总消息条数
    private String pirivateMsgCount;//私聊消息条数
    private String groupMsgCount;//群消息条数
    private String meetMsgCount;//会议消息条数
    private  String textMsgCount;//文字消息条数
    private String picMsgCount;//图片消息条数
    private String fileMsgCount;//附件消息条数
}