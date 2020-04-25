package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

public class InternalException extends BaseException {
        public InternalException(String msg) {
            super(msg, CommonConstants.EX_OTHER_CODE);
        }
    }
