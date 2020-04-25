package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 负责内部服务token异常
 * @author 协同设计小组
 * @date 2017/9/10
 */
public class ClientTokenException extends BaseException {
    public ClientTokenException(int status,String message) {
        super(message, status);
    }
}
