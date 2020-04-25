package com.github.hollykunge.security.common.exception.service;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BizException;

/**
 * @author: zhhongyu
 * @description: 客户端无效参数异常
 * @since: Create in 10:03 2020/4/24
 */
public class ClientParameterInvalid extends BizException {
    public ClientParameterInvalid(String message){
        super(message, CommonConstants.EX_BIZ_CLIENT_PARAMETER);
    }
}
