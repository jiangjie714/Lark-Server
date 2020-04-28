package com.github.hollykunge.security.common.exception;

import lombok.Data;

/**
 * @author: zhhongyu
 * @description: 业务异常定义基类
 * @since: Create in 14:51 2020/4/23
 */
@Data
public class BizException extends RuntimeException{
    private int status = 40000;

    public BizException() {
    }

    public BizException(String message,int status) {
        super(message);
        this.status = status;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
