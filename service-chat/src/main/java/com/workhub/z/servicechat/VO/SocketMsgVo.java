package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhuqz
 * description: 发送给消息给socket端
 * date:2019/12/27 16:02
 **/
@Data
public class SocketMsgVo implements Serializable {
    private static final long serialVersionUID = 3317552605396382844L;
    /**数据id 确认消息接收成功使用*/
    String id;
    /**发送人*/
    String sender = "system";
    /**接收人,多个逗号分割，且多个人情况下confirmFlg应是fasle*/
    String receiver;
    /**编码**/
    String code;
    /**消息内容**/
    Object msg;
    /**是否需要应答*/
    Boolean confirmFlg = false;
}
