package com.github.hollykunge.security.mq.constants;

/**
 * @author: zhhongyu
 * @description: 交换机常量类
 * @since: Create in 10:45 2020/3/30
 */
public class RabbiMqExchangeConstant {
    /** 公告通知交换机名称*/
    public static final String NOTICE_EXCHANGE = "noticeExchange";
    /**
     * 通知死信队列交换机名称
     */
    public final static String NOTICE_DEAD_EXCHANGENAME = "notice_dead_exchange";
    /**
     * 未消费掉的人员或组织使用的交换机
     */
    public static final String ADMIN_USERORORG_EXCHANGE = "adminUserAndOrgExchange";
    /**
     * ADMIN组织用户交换机名称
     */
    public final static String WERSERVICE_ADMIN_USERANDORG_EXCHANGE = "webAdminUserAndOrgExchange";
    public static final String EXCHANGE_CONTACT = "exchange_contact";
}
