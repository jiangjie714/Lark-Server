package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 负责内部服务调用可用性异常
 * @author 协同设计小组
 * @date 2017/9/10
 */
public class ServerInvalidException extends BaseException {
    public ServerInvalidException(String message) {
        super(message, CommonConstants.EX_SERVICE_INVALID_CODE);
    }
}
