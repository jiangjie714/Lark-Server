package com.github.hollykunge.security.auth.client.jwt;

import com.github.hollykunge.security.auth.client.config.UserAuthConfig;
import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.auth.common.util.jwt.JWTHelper;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.service.UserTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 协同设计小组 on 2017/9/15.
 */
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;
    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
        }catch (ExpiredJwtException ex){
            throw new UserTokenException(CommonConstants.EX_USER_TOKEN_EXPIRED,"token过期了!");
        }catch (SignatureException ex){
            throw new UserTokenException(CommonConstants.EX_USER_TOKEN_SIGNATURE,"token签名错误!");
        }catch (IllegalArgumentException ex){
            throw new UserTokenException(CommonConstants.EX_USER_TOKEN_INVALID,"token为null或者空!");
        }
    }
}
