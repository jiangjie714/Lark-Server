package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/4/27 14:01
 * @description: 导出消息结构
 */
@Data
public class ExportMsgVo implements Serializable {
    private static final long serialVersionUID = -1364227855544188967L;
    /**发送人*/
    private String sender;
    /**接收人*/
    private String receiver;
    /**文字 附件 图片*/
    private String msgType;
    /**内容*/
    private String msgContent;
    /**发送时间*/
    private String sendTime;
    /**非密 密码 机密*/
    private String levels;
}