package com.github.hollykunge.security.task.config;

import com.github.hollykunge.security.task.constant.TaskCommon;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation 交换机队列绑定配置
 */
@Configuration
public class ExchangeBindingQueue {

    @Autowired
    private ExchangeConfig exchangeConfig;

    @Autowired
    private QueueConfig queueConfig;

    @Bean
    @Order(value = 1)
    public Binding bindingInviteMemberDeadQueue() {
        return BindingBuilder.bind(queueConfig.inviteMemberDeadQueue()).to(exchangeConfig.inviteMemberDeadExchange()).with(TaskCommon.INVITEMEMBER_DEAD_ROUTING_KEY);
    }

    @Bean
    @Order(value = 2)
    public Binding bindingInviteMemberQueue() {
        return BindingBuilder.bind(queueConfig.inviteMemberQueue()).to(exchangeConfig.inviteMemberExchange()).with(TaskCommon.INVITEMEMBER_ROUTING_KEY);
    }
}
