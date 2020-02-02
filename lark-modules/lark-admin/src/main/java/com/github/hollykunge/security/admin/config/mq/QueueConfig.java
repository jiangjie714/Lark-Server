package com.github.hollykunge.security.admin.config.mq;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.common.constant.CommonConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列配置  可以配置多个队列
 * @author zhhongyu
 * @create 2019/5/9 13:25
 */
@Configuration
public class QueueConfig {


    @Bean
    @Order(value = 4)
    public Queue noticDeadQueue() {
        Queue queue = new Queue(CommonConstants.NOTICE_DEAD_QUEUENAME, true);
        return queue;
    }

    @Bean
    @Order(value = 5)
    public Queue noticToChatQueue() {
        Queue queue = new Queue(CommonConstants.NOTICE_TOWECHAT_QUEUE_NAMA, true);
        return queue;
    }

    @Bean
    @Order(value = 6)
    public Queue noticeQueue() {

        // 消息发布队列绑定死信（备胎交换机和备胎队列）
        Map<String, Object> args = new HashMap<>(2);
        // 绑定死交换机
        args.put(AdminCommonConstant.DEAD_LETTER_QUEUE_KEY, AdminCommonConstant.NOTICE_DEAD_EXCHANGENAME);
        args.put(AdminCommonConstant.DEAD_LETTER_ROUTING_KEY, AdminCommonConstant.NOTICE_DEAD_ROUTING_KEY);
        /**
         durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
         auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
         exclusive  表示该消息队列是否只在当前connection生效,默认是false
         */
        return new Queue(CommonConstants.NOTICE_QUEUE_NAMA,true,false,false,args);
    }

    @Bean
    @Order(value = 7)
    public Queue adminToUser() {
        Queue queue = new Queue(CommonConstants.ADMIN_UNACK_USER, true);
        return queue;
    }

    @Bean
    @Order(value = 8)
    public Queue adminToOrg() {
        Queue queue = new Queue(CommonConstants.ADMIN_UNACK_ORG, true);
        return queue;
    }
}