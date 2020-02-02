package com.workhub.z.servicechat.server;

/**
 * @author tanyaowu
 *
 */
public class Const {
    /**
     * 用于系统消息，每个登录用户都要加入系统消息组
     * 系统通知
     */
    public static final String GROUP_SYS = "Iwork-Message";

    /**
     * 加入群组
     */
    public static final int JOIN_GROUP = 2;

    /**
     * 退出群组
     */
    public static final int EXIT_GROUP = 3;

    /**
     * ping
     */
    public static final int PING = 1;

    /**
     * 私聊消息
     */
    public static final int SEND_USER = 4;

    /**
     * 群组消息
     */
    public static final int SEND_GROUP = 5;

    /**
     * 发送消息
     */
    public static final int SEND_MSG = 5;

    /**
     * 关闭群组
     */
    public static final int CLOSE_GROUP = 6;

    /**
     * 发送文件
     */
    public static final int SEND_FILE = 7;

    /**
     * 发送系统消息
     */
    public static final int SEND_SYS = 0;
}
