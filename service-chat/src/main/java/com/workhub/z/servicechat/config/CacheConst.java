package com.workhub.z.servicechat.config;

/**
 * @author:zhuqz
 * description:缓存常量
 * date:2019/10/24 11:16
 **/
public class CacheConst {
    /**涉密词汇缓存*/
    public static final String SECRET_WORDSCACHE = "lark_chat_secret_words";
    /**涉密词汇-秘密-key*/
    public static final String SECRET_WORDS_40 = "chat_secret_words_40";
    /**涉密词汇-机密-key*/
    public static final String SECRET_WORDS_60 = "chat_secret_words_60";
    /**用户在线缓存 一级目录(users_on_line) */
    public static final String userOnlineCahce = "users_on_line:";
    /**用户缓存群列表id */
    public static final String userGroupIds = "user_group";
    /**用户缓存会议列表id */
    public static final String userMeetIds = "user_meet";
    /**换存用户信息*/
    public static final String USER_INF = "user_inf";
    /**在线人员缓存列表*/
    public static final String USER_ONLINE_LIST = "user_online_list";
}
