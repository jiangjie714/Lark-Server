package com.github.hollykunge.security.common.exception.service;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * @author LARK
 */
public class ServiceHandleException extends BaseException {

    public ServiceHandleException(String className, String message) {
        super(message + "the error class is:" + className, CommonConstants.EX_SERVICE_INVALID_CODE);
    }
}
