package com.github.hollykunge.security.common.exception.auth;


import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 外部访问token异常返回类型
 * @author 协同设计小组
 * @date 2017/9/8
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
