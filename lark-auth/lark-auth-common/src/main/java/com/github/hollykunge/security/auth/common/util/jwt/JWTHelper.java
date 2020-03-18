package com.github.hollykunge.security.auth.common.util.jwt;

import com.github.hollykunge.security.auth.common.constatns.CommonConstants;
import com.github.hollykunge.security.auth.common.util.StringHelper;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;

/**
 * Created by 协同设计小组 on 2017/9/10.
 */
public class JWTHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();
    /**
     * 密钥加密token
     *
     * @param jwtInfo
     * @param priKeyPath
     * @param expire
     * @return
     * @throws Exception
     */
    public static String generateToken(IJWTInfo jwtInfo, String priKeyPath, int expire) throws Exception {
        String compactJws = Jwts.builder()
                .setSubject(jwtInfo.getUniqueName())
                .claim(CommonConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(CommonConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(CommonConstants.JWT_KEY_SECRETLEVEL,jwtInfo.getSecretLevel())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath))
                .compact();
        return compactJws;
    }

    /**
     * 密钥加密token
     *
     * @param jwtInfo
     * @param priKey
     * @param expire
     * @return
     * @throws Exception
     */
    public static String generateToken(IJWTInfo jwtInfo, byte priKey[], int expire) throws Exception {
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(jwtInfo.getUniqueName())
                .claim(CommonConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(CommonConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(CommonConstants.JWT_KEY_SECRETLEVEL,jwtInfo.getSecretLevel());
        if(expire == 0){
            builder.setExpiration(null);
        }else{
            builder.setExpiration(DateTime.now().plusSeconds(expire).toDate());
        }
        builder.signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey));
        String compactJws =  builder.compact();
        return compactJws;
    }

    /**
     * 公钥解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws(token);
        return claimsJws;
    }
    /**
     * 公钥解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
        return claimsJws;
    }
    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param pubKeyPath
     * @return
     * @throws Exception
     */
    public static IJWTInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(), StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_USER_ID)),
                StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_NAME)),
                StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_SECRETLEVEL)),
                StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_PATHCODE)));
    }
    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static IJWTInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(), StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_USER_ID)),
                StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_NAME)),
                StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_SECRETLEVEL)),
                StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_PATHCODE)));
    }
}
