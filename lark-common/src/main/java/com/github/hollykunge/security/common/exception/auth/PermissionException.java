package com.github.hollykunge.security.common.exception.auth;

import com.github.hollykunge.security.common.exception.BaseException;

/**
 * @author: zhhongyu
 * @description: 权限异常
 * @since: Create in 8:36 2020/4/21
 */
public class PermissionException extends BaseException {
    public PermissionException(int status,String message){
        super(message,status);
    }

}
