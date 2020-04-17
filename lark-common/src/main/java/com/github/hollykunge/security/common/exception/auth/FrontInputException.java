package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 用户输入型异常
 * @author LARK
 */
public class FrontInputException extends BaseException {
    public FrontInputException(String message) {
        super(message, CommonConstants.EX_FRONT_INVALID_CODE);
    }
}
