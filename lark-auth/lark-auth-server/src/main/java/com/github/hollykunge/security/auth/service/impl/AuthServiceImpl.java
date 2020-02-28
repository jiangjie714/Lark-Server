package com.github.hollykunge.security.auth.service.impl;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.auth.common.util.jwt.JWTInfo;
import com.github.hollykunge.security.auth.feign.IUserService;
import com.github.hollykunge.security.auth.service.AuthService;
import com.github.hollykunge.security.auth.util.user.JwtTokenUtil;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.auth.ClientInvalidException;
import com.github.hollykunge.security.common.exception.auth.UserTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        //fansq 20-2-24 添加异常抛出 UserTokenException 40101
        if(StringUtils.isEmpty(pid)||StringUtils.isEmpty(password)){
            throw  new UserTokenException("User name or password is empty");
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
    public IJWTInfo tokenValidate(String token) throws Exception {
        return jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public Boolean invalid(String token) {
        // TODO: 2017/9/11 注销token
        return null;
    }

    @Override
    public String refresh(String oldToken) {
        // TODO: 2017/9/11 刷新token
        return null;
    }
}
