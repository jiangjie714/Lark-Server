package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/2/29 17:38
 * @description: 私聊接收人打开了接收消息的页面
 */
@Data
public class SocketMsgReaderVo implements Serializable {
    private static final long serialVersionUID = 206279445169416863L;
    //发消息人id
    private String senderId;
    //点开收到消息页面人的id
    private String readerId;
}