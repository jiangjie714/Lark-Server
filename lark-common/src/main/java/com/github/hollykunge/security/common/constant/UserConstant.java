package com.github.hollykunge.security.common.constant;

/**
 * 盐和人员密级
 *
 * @author 协同设计小组
 * @create 2017-06-14 8:36
 */
public class UserConstant {

    public static int PW_ENCORDER_SALT = 12;

    /**
     * 非密人员
     */
    public static final String USER_CLASS_FM = "FM";
    /**
     * 一般人员
     */
    public static final String USER_CLASS_YB = "YB";
    /**
     * 重要人员
     */
    public static final String USER_CLASS_ZY = "ZY";
    /**
     * 核心人员
     */
    public static final String USER_CLASS_HX = "HX";

}
