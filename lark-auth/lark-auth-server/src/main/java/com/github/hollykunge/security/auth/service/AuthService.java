package com.github.hollykunge.security.auth.service;


/**
 * @author LARK
 */
public interface AuthService {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String login(String username, String password) throws Exception;

    /**
     * 刷新token
     * @param oldToken
     * @return
     * @throws Exception
     */
    String refresh(String oldToken) throws Exception;

    /**
     * 验证token
     * @param token
     * @throws Exception
     */
    void validate(String token) throws Exception;
}
