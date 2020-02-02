package com.github.hollykunge.security.common.parser.impl;

import com.github.hollykunge.security.common.config.AutoConfiguration;
import com.github.hollykunge.security.common.parser.IUserSecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhhongyu
 * @description: 获取当前登录人的密级实现
 * @since: Create in 9:32 2019/12/3
 */
public class UserSecretImpl implements IUserSecretGenerator {
    @Autowired
    private HttpServletRequest request;

    @Override
    public String getUserSecret() {
        return request.getHeader("userSecretLevel");
    }
}
