package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
*@Description: 负责密级异常
*@Author: 忠
*@date: 2019/5/16
*/
public class SecretLevelsException extends BaseException {
    public SecretLevelsException(String message) {
        super(message, CommonConstants.EX_SECRET_LEVELS);
    }
}
