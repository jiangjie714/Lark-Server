package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 负责用户身份验证的异常
 * @author 协同设计小组
 * @date 2017/9/8
 */
public class UserTokenException extends BaseException {
    public UserTokenException(int status,String message) {
        super(message, status);
    }
}
