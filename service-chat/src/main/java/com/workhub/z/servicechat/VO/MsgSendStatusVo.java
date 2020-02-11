package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description: 消息发送进行持久化是否成功
 * date:2020/2/7 19:39
 **/
@Data
public class MsgSendStatusVo {
    /**后端生成的消息id*/
    private String id;
    /**前端消息id*/
    private String oId;
    /**是否发送成功true，false*/
    private Boolean status = true;
    /**提示说明*/
    private String content = "成功";
}
