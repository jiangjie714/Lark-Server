package com.github.hollykunge.security.common.exception.server;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;

/**
 * @author: zhhongyu
 * @description: feign执行异常
 * @since: Create in 10:51 2020/4/28
 */
public class FeignExecutException extends BaseException {
    public FeignExecutException(String message){
        super(message, CommonConstants.EX_FEIGN_EXECUT);
    }
}
