package com.github.hollykunge.security.mq.constants;

/**
 * @author: zhhongyu
 * @description: 路由键常量类
 * @since: Create in 11:14 2020/3/30
 */
public class RabbitMqRoutingKeyConstant {
    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    /**
     * 通知死信队列路由键
     */
    public final static String NOTICE_DEAD_ROUTING_KEY = "notice_dead_routing_key";
    /**
     * 通知公告发送到门户服务路由键
     */
    public static final String NOTICE_TOPORTAL_ROTEING_KEY = "notic_to_portal";
    /**
     * 通知公告发送到研讨服务路由键
     */
    public static final String NOTICE_TOCHAT_ROTEING_KEY = "notic_to_chat";
    /**
     * 路由键
     */
    public static final String ADMIN_UNACK_USER_KEY = "admin_unack_user_key";
    /**
     * 路由键
     */
    public static final String ADMIN_UNACK_ORG_KEY = "admin_unack_org_key";
    /**
     * fansq 发送取消公告到portal服务路由键
     */
    public static final String CANCEL_NOTICE_TOPORTAL_ROTEING_KEY = "cancel_notic_to_portal";
    /**
     * 用户路由键
     */
    public static final String ADMINUSER_ROTEING_KEY = "admin_user";
    /**
     * 组织路由键
     */
    public static final String ADMINORG_ROTEING_KEY = "admin_org";

    public static final String ROUTINGKEY_CONTACT = "routingkey_contact";

}
