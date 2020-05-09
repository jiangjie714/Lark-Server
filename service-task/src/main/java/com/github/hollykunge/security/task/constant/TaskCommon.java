package com.github.hollykunge.security.task.constant;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation task 公共常量类
 */
public class TaskCommon {

    /**
     * 邀请成员交换机
     */
    public static final String INVITEMEMBER_EXCHANGE = "task_inviteMember_EXCHANGE";
    /**
     * 邀请成员队列
     */
    public static final String INVITEMEMBER_QUEUE = "task_inviteMember_queue";
    /**
     * 邀请成员路由键
     */
    public static final String INVITEMEMBER_ROUTING_KEY = "task_inviteMember_routing_key";
    /**
     * 邀请成员死信交换机
     */
    public final static String INVITEMEMBER_DEAD_EXCHANGENAME = "task_inviteMember_dead_exchange";
    /**
     * 邀请成员死信队列
     */
    public final static String INVITEMEMBER_DEAD_QUEUE = "task_inviteMember_dead_queue";

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
    public final static String INVITEMEMBER_DEAD_ROUTING_KEY = "task_inviteMember_dead_routing_key";

    /**
     * 数字1
     */
    public final static Integer NUMBER_ONE = 1;
    public final static String NUMBER_ONE_STRING = "1";
    /**
     * 数字0
     */
    public final static Integer NUMBER_ZERO = 0;
}
