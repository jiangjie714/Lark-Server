package com.workhub.z.servicechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/4/2 15:29
 * @description: 在线客服配置
 */
@Component
public class OnLineServerConfig {
    //在线客服的id，逗号分隔
    private static final  String ONLINE_SERVER_IDS = "YE016KXb,123457,123458";
    //客服默认id
    private static  String defaultUserId ;
    @Value("${online-server-id}")
    void setOnlineServerId(String serverid){
        OnLineServerConfig.defaultUserId =  serverid;
    }
    //获取在线客服列表
    public static List<String> getOnlineServerList() {
        return Arrays.asList(ONLINE_SERVER_IDS.split(","));
    }
    //获取默认客服id
    public static String  getOnlineServerId(){
        return defaultUserId;
    }
}