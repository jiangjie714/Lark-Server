package com.github.hollykunge.security.common.exception.server;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 服务器处理时异常
 * @author 协同设计小组
 * @date 2017/9/10
 */
public class ServerHandlerException extends BaseException {
    public ServerHandlerException(String message) {
        super(message, CommonConstants.EX_SERVICE_INVALID_CODE);
    }
}
