package com.workhub.z.servicechat.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhuqz
 * description:应答前端消息体
 * date:2019/10/16 14:32
 **/
@Data
public class AnswerToFrontReponse implements Serializable {
    /**编码 与 MessageType编码一一对应*/
    private int code;
    /**消息内容*/
    private Object data;
}
