package com.workhub.z.servicechat.config;

/**
 * @auther: zhuqz
 * @date: 2020/4/15 11:23
 * @description: 获取gate请求过滤后相关参数常量
 */
public class GateRequestHeaderParamConfig {
    private  static String  pidInHeaderRequest = "pid";//身份证号
    private  static String  clientIpInHeaderRequest = "clientIp";//"clientIp","userHost";//客户端ip
    private  static String  userNameInHeaderRequest = "userName";//用户id
    private  static String  userIdInHeaderRequest = "userId";//用户名字

    public static String getPid() {
        return pidInHeaderRequest;
    }

    public static String getClientIp() {
        return clientIpInHeaderRequest;
    }

    public static String getUserName() {
        return userNameInHeaderRequest;
    }

    public static String getUserId() {
        return userIdInHeaderRequest;
    }
}