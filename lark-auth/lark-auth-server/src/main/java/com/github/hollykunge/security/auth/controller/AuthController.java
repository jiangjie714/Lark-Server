package com.github.hollykunge.security.auth.controller;

import com.github.hollykunge.security.auth.service.AuthService;
import com.github.hollykunge.security.auth.util.user.JwtAuthenticationRequest;
import com.github.hollykunge.security.auth.util.user.JwtAuthenticationResponse;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.UserInvalidException;
import com.github.hollykunge.security.common.exception.auth.UserTokenException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.util.IntranetRequestHeaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import zipkin2.Call;

import javax.servlet.http.HttpServletRequest;

/**
 * @author holly
 */
@RestController
@RequestMapping("jwt")
@Slf4j
public class AuthController {

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Value("${auth.user.defaultPassword}")
    private String defaultPassword;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception {
        final String token;
        String pid = IntranetRequestHeaderUtils.getDnName(request);
        if (pid==""||pid==null){
            token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } else {
            token = authService.login(pid, defaultPassword);
        }

        return new ObjectRestResponse().data(new JwtAuthenticationResponse(token)).msg("获取token成功");
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws Exception {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            throw new UserTokenException("用户token刷新失败");
        } else {
            return new ObjectRestResponse().data(new JwtAuthenticationResponse(refreshedToken)).msg("刷新token成功");
        }
    }

    @RequestMapping(value = "verify", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<?> verify(String token) throws Exception {
        authService.validate(token);
        return new ObjectRestResponse<>().rel(true);
    }
}
