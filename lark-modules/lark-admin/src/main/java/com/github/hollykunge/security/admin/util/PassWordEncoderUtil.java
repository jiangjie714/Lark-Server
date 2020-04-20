package com.github.hollykunge.security.admin.util;

import com.github.hollykunge.security.common.constant.UserConstant;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author: zhhongyu
 * @description: 用户密码加密工具类
 * @since: Create in 13:16 2020/4/18
 */
public class PassWordEncoderUtil {
    public static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT);
}
