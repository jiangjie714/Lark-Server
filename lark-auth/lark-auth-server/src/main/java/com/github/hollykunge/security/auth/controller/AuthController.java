package com.github.hollykunge.security.auth.controller;

import com.github.hollykunge.security.auth.service.AuthService;
import com.github.hollykunge.security.auth.util.user.JwtAuthenticationRequest;
import com.github.hollykunge.security.auth.util.user.JwtAuthenticationResponse;
import com.github.hollykunge.security.common.exception.server.ServerHandlerException;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.util.IntranetRequestHeaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
        String pid = request.getHeader("pid");
        //如果用户名和密码存在的话，使用用户名和密码登录
        if (authenticationRequest != null &&
                !StringUtils.isEmpty(authenticationRequest.getUsername()) &&
                !StringUtils.isEmpty(authenticationRequest.getPassword())) {
            token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }
        //内网使用密匙登录
        else if(!StringUtils.isEmpty(pid)){
            token = authService.login(pid, defaultPassword);
        }
        //无效登录
        else{
            throw new ClientParameterInvalid("无效的登录请求，请检查用户身份信息是否正确。");
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
            throw new ServerHandlerException("用户token刷新失败。");
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
