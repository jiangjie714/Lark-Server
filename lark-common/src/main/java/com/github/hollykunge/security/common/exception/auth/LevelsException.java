package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
*@Description: 密级权限不匹配，抛出异常
*@Author: 忠
*@date: 2019/5/16
*/
public class LevelsException extends BaseException {
    public LevelsException(String message) {
        super(message, CommonConstants.EX_LEVELS);
    }
}
