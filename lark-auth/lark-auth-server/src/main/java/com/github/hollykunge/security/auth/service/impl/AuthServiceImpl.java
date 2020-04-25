package com.github.hollykunge.security.auth.service.impl;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.auth.common.util.jwt.JWTInfo;
import com.github.hollykunge.security.auth.feign.IUserService;
import com.github.hollykunge.security.auth.service.AuthService;
import com.github.hollykunge.security.auth.util.user.JwtTokenUtil;
import com.github.hollykunge.security.common.exception.service.UserInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author LARK
 */
@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;

    @Autowired
    public AuthServiceImpl(
            JwtTokenUtil jwtTokenUtil,
            IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(String pid, String password) throws Exception {
        if(StringUtils.isEmpty(pid)||StringUtils.isEmpty(password)){
            throw new UserInvalidException("用户名和密码为空");
        }
        AdminUser info = userService.validate(pid,password);
        String token = "";
        if (!StringUtils.isEmpty(info.getId())) {
            token = jwtTokenUtil.generateToken(new JWTInfo(info.getPId(), info.getId() + "", info.getName(),info.getSecretLevel()));
        }
        return token;
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public String refresh(String oldToken) throws Exception {
        return jwtTokenUtil.generateToken(jwtTokenUtil.getInfoFromToken(oldToken));
    }
}
