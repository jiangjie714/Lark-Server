package com.github.hollykunge.security.mq.config;

import com.github.hollykunge.security.mq.constants.RabbitMqRoutingKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * RabbitMq配置
 * @author zhhongyu
 * @create 2019/5/9 13:37
 */
@Configuration
@Slf4j
public class RabbitMqBindingConfig {

    @Autowired
    private QueueConfig queueConfig;
    @Autowired
    private ExchangeConfig exchangeConfig;

    /**
     将消息队列和交换机进行绑定
     */
    @Bean
    @Order(value = 9)
    public Binding bindingPortal() {
        return BindingBuilder.bind(queueConfig.noticeQueue()).to(exchangeConfig.directExchange()).with(RabbitMqRoutingKeyConstant.NOTICE_TOPORTAL_ROTEING_KEY);
    }

    /**
     * 死信队列与死信交换机进行绑定
     * @return
     */
    @Bean
    @Order(value = 10)
    public Binding bindingDeadExchange() {
        return BindingBuilder.bind(queueConfig.noticeDeadQueue()).to(exchangeConfig.noticeDeadExchange()).with(RabbitMqRoutingKeyConstant.DEAD_LETTER_ROUTING_KEY);
    }

    /**
     * 通知公告发送到研讨服务队列
     * @return
     */
    @Bean
    @Order(value = 11)
    public Binding bindingChat() {
        return BindingBuilder.bind(queueConfig.noticeToChatQueue()).to(exchangeConfig.directExchange()).with(RabbitMqRoutingKeyConstant.NOTICE_TOCHAT_ROTEING_KEY);
    }

    @Bean
    @Order(value = 12)
    public Binding bindingAdminUser() {
        return  BindingBuilder.bind(queueConfig.adminToUser()).to(exchangeConfig.adminDirectExchange()).with(RabbitMqRoutingKeyConstant.ADMIN_UNACK_USER_KEY);
    }

    @Bean
    @Order(value = 13)
    public Binding bindingAdminOrg() {
        return  BindingBuilder.bind(queueConfig.adminToOrg()).to(exchangeConfig.adminDirectExchange()).with(RabbitMqRoutingKeyConstant.ADMIN_UNACK_ORG_KEY);
    }

    /**
     * fansq
     * 绑定 cancelNoticeQueue noticeExchange
     * @return
     */
    @Bean
    @Order(value = 14)
    public Binding bindingCancelPortal() {
        return BindingBuilder.bind(queueConfig.cancelNoticeQueue()).to(exchangeConfig.directExchange()).with(RabbitMqRoutingKeyConstant.CANCEL_NOTICE_TOPORTAL_ROTEING_KEY);
    }

    @Bean
    public Binding orgBinding(){
        Binding binding = BindingBuilder.bind(queueConfig.adminOrgQueue()).to(exchangeConfig.adminUserAndOrgExchange()).with(RabbitMqRoutingKeyConstant.ADMINORG_ROTEING_KEY);
        return binding;
    }
    @Bean
    public Binding userBinding(){
        Binding binding = BindingBuilder.bind(queueConfig.adminUserQueue()).to(exchangeConfig.adminUserAndOrgExchange()).with(RabbitMqRoutingKeyConstant.ADMINUSER_ROTEING_KEY);
        return binding;
    }

    /**
     * 协同编辑组织队列绑定交换机
     * @return
     */
    @Bean
    public Binding oneDocOrgBinding(){
        Binding binding = BindingBuilder.bind(queueConfig.oneDocOrgQueue()).to(exchangeConfig.adminUserAndOrgExchange()).with(RabbitMqRoutingKeyConstant.ADMINORG_ROTEING_KEY);
        return binding;
    }

    /**
     * 协同编辑用户队列绑定交换机
     * @return
     */
    @Bean
    public Binding oneDocUserBinding(){
        Binding binding = BindingBuilder.bind(queueConfig.oneDocUserQueue()).to(exchangeConfig.adminUserAndOrgExchange()).with(RabbitMqRoutingKeyConstant.ADMINUSER_ROTEING_KEY);
        return binding;
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueConfig.queueContact()).to(exchangeConfig.defaultExchange()).with(RabbitMqRoutingKeyConstant.ROUTINGKEY_CONTACT);
    }

    @Bean
    public Binding onedocUnackUser() {
        return BindingBuilder.bind(queueConfig.onedocUserUnack()).to(exchangeConfig.onedocUserAndOrgExchange()).with(RabbitMqRoutingKeyConstant.ROUTINGKEY_ONE_DOC_USERUNACK);
    }

    @Bean
    public Binding onedocUnackOrg() {
        return BindingBuilder.bind(queueConfig.onedocOrgUnack()).to(exchangeConfig.onedocUserAndOrgExchange()).with(RabbitMqRoutingKeyConstant.ROUTINGKEY_ONE_DOC_ORGUNACK);
    }
}
