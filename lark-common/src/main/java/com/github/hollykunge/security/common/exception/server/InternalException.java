package com.github.hollykunge.security.common.exception.server;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * http网络解析异常
 */
public class InternalException extends BaseException {
        public InternalException(String msg) {
            super(msg, CommonConstants.EX_OTHER_CODE);
        }
    }
