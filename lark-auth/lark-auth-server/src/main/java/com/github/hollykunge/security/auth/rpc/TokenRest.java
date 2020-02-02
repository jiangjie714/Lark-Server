package com.github.hollykunge.security.auth.rpc;

import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.auth.service.AuthService;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * token验证并返回userId
 *
 * @author: holly
 * @since: 2019/4/28
 */
@RestController
@RequestMapping("api")
public class TokenRest {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/token/verify", method = RequestMethod.GET)
    public ObjectRestResponse<?> verify(String token) throws Exception {
        IJWTInfo ijwtInfo = (IJWTInfo) authService.tokenValidate(token);
        return new ObjectRestResponse().data(ijwtInfo.getId());
    }
    @RequestMapping(value = "/token/generate", method = RequestMethod.GET)
    public ObjectRestResponse<?> generate(String username, String password) throws Exception {
        final String token = authService.login(username, password);
        return new ObjectRestResponse().data(token).msg("获取token成功");
    }
}
