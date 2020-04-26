package com.github.hollykunge.security.common.exception.service;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.BizException;

/**
 * 负责用户名或密码异常
 * @author LARK
 */
public class UserInvalidException extends BizException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}
