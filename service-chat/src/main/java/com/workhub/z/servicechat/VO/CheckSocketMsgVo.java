package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @auther: zhuqz
 * @date: 2020/3/14 17:20
 * @description: 校验消息是否合法
 */
@Data
public class CheckSocketMsgVo {
    //校验结果true通过
    private Boolean res = true;
    //校验结果说明
    private String msg = "通过";
}