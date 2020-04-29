package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/4/10 10:19
 * @description: 消息撤销
 */
@Data
public class MsgCancelVo implements Serializable {
    private static final long serialVersionUID = -8619771201978735712L;
    private String msgId;//撤销id
    private String receiver;//接收人（对应前端来说可能是群或者会议id）
    private String type;//0私聊1群聊2会议
    private String cancelUser;//撤销人
}