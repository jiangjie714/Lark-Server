package com.workhub.z.servicechat.VO;

import com.github.hollykunge.security.common.vo.mq.MsgQueue;
import lombok.Data;

@Data
public class SystemMsgVo {
//    系统消息编码 SYS_MSG 2
    private int code;
//    消息内容
    private MsgQueue msgQueue;
}
