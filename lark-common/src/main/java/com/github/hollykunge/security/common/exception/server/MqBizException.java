package com.github.hollykunge.security.common.exception.server;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * @author: zhhongyu
 * @description: mq消费消息异常
 * @since: Create in 19:16 2020/4/25
 */
public class MqBizException extends BaseException {
    public MqBizException(String message){
        super(message, CommonConstants.EX_MQ_CONSUMER_BIZ);
    }
}
