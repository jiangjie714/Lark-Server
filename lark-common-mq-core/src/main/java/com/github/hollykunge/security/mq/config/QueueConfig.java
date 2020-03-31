package com.github.hollykunge.security.mq.config;

import com.github.hollykunge.security.mq.constants.RabbiMqExchangeConstant;
import com.github.hollykunge.security.mq.constants.RabbitMqQueConstant;
import com.github.hollykunge.security.mq.constants.RabbitMqRoutingKeyConstant;
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
    public Queue noticeDeadQueue() {
        return new Queue(RabbitMqQueConstant.NOTICE_DEAD_QUEUENAME, true);
    }

    @Bean
    @Order(value = 5)
    public Queue noticeToChatQueue() {
        return new Queue(RabbitMqQueConstant.NOTICE_TOWECHAT_QUEUE_NAMA, true);
    }

    @Bean
    @Order(value = 6)
    public Queue noticeQueue() {

        // 消息发布队列绑定死信（备胎交换机和备胎队列）
        Map<String, Object> args = new HashMap<>(2);
        // 绑定死交换机
        args.put(RabbitMqRoutingKeyConstant.DEAD_LETTER_QUEUE_KEY, RabbiMqExchangeConstant.NOTICE_DEAD_EXCHANGENAME);
        args.put(RabbitMqRoutingKeyConstant.DEAD_LETTER_ROUTING_KEY, RabbitMqRoutingKeyConstant.NOTICE_DEAD_ROUTING_KEY);
        /**
         durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
         auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
         exclusive  表示该消息队列是否只在当前connection生效,默认是false
         */
        return new Queue(RabbitMqQueConstant.NOTICE_QUEUE_NAMA,true,false,false,args);
    }

    @Bean
    @Order(value = 7)
    public Queue adminToUser() {
        return new Queue(RabbitMqQueConstant.ADMIN_UNACK_USER, true);
    }

    @Bean
    @Order(value = 8)
    public Queue adminToOrg() {
        return new Queue(RabbitMqQueConstant.ADMIN_UNACK_ORG, true);
    }

    /**
     * fansq
     * 20-2-18
     * 添加取消公告队列
     * @return
     */
    @Bean
    @Order(value = 9)
    public Queue cancelNoticeQueue() {
        return new Queue(RabbitMqQueConstant.CANCEL_NOTICE_QUEUE_NAME, true);
    }

    @Bean
    public Queue adminUserQueue(){
        return new Queue(RabbitMqQueConstant.ADMINUSER_QUEUE_NAME,true);
    }
    @Bean
    public Queue adminOrgQueue(){
        return new Queue(RabbitMqQueConstant.ADMINORG_QUEUE_NAME,true);
    }

    /**
     * 推送协同编辑用户队列
     * @return
     */
    @Bean
    public Queue oneDocUserQueue(){
        return new Queue(RabbitMqQueConstant.ONEDOCUSER_QUEUE_NAME,true);
    }

    /**
     * 推送协同编辑组织队列
     * @return
     */
    @Bean
    public Queue oneDocOrgQueue(){
        return new Queue(RabbitMqQueConstant.ONEDOCORG_QUEUE_NAME,true);
    }

    /**
     * 获取队列A
     * @return
     */
    @Bean
    public Queue queueContact() {
        return new Queue(RabbitMqQueConstant.QUEUE_CONTACT, true); //队列持久
    }

    @Bean
    public Queue onedocOrgUnack() {
        return new Queue(RabbitMqQueConstant.ONEDOC_ORG, true); //队列持久
    }
    @Bean
    public Queue onedocUserUnack() {
        return new Queue(RabbitMqQueConstant.ONEDOC_USER, true); //队列持久
    }
}
