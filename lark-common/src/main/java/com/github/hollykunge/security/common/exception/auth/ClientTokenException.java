package com.github.hollykunge.security.common.exception.auth;


import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * 内部访问token异常返回类型
 * @author 协同设计小组
 * @date 2017/9/10
 */
public class ClientTokenException extends BaseException {
    public ClientTokenException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
