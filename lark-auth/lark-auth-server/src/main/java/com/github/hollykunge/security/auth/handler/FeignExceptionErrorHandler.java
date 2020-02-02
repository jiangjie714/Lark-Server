package com.github.hollykunge.security.auth.handler;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.exception.BaseException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * feign客户端异常处理
 * @author zhhongyu
 */
@Configuration
@Slf4j
public class FeignExceptionErrorHandler implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        try {
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader());
                log.error(body);
                BaseException ei = JSON.parseObject(body.getBytes("UTF-8"), BaseException.class);
                return ei;
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new InternalException(exception.getMessage());
        }
        return new InternalException("系统异常,请联系管理员");
    }

    public class InternalException extends RuntimeException {
        public InternalException(String msg) {
            super(msg);
        }
    }
}