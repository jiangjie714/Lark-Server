package com.github.hollykunge.security.mq.constants;

/**
 * @author: zhhongyu
 * @description: rabbitmq队列常量类
 * @since: Create in 10:44 2020/3/30
 */
public class RabbitMqQueConstant {
    /**
     * 通知死信队列名称
     */
    public final static String NOTICE_DEAD_QUEUENAME = "notic_dead_queue";
    /**
     * 公告推送到研讨服务队列名称
     */
    public static final String NOTICE_TOWECHAT_QUEUE_NAMA = "noticeToChatService";
    /** 公告队列名称 */
    public static final String NOTICE_QUEUE_NAMA = "noticeQueue";
    /**
     * admin服务中人员未被消费队列
     */
    public static final String ADMIN_UNACK_USER = "admin_unack_user_queue";
    /**
     * admin服务中组织未被的队列
     */
    public static final String ADMIN_UNACK_ORG = "admin_unack_org_queue";
    /** 取消公告分布*/
    public static final String CANCEL_NOTICE_QUEUE_NAME = "cancelNoticeQueue";

    /**
     * 用户队列名称
     */
    public final static String ADMINUSER_QUEUE_NAME = "adminUserQueue";
    /**
     * 组织队列名称
     */
    public final static String ADMINORG_QUEUE_NAME = "adminOrgQueue";

    /**
     * 推送到协同编辑用户队列名称
     */
    public final static String ONEDOCUSER_QUEUE_NAME = "oneDocUserQueue";
    /**
     * 推送到协同编辑组织队列名称
     */
    public final static String ONEDOCORG_QUEUE_NAME = "oneDocOrgQueue";
}
