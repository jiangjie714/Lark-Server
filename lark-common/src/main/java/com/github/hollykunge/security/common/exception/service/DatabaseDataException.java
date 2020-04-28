package com.github.hollykunge.security.common.exception.service;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BizException;

/**
 * @author: zhhongyu
 * @description: 数据库数据查询后运行时异常
 * @since: Create in 10:22 2020/4/24
 */
public class DatabaseDataException extends BizException {
    public DatabaseDataException(String message){
        super(message, CommonConstants.EX_BIZ_SERVER_DATABASE);
    }
}
