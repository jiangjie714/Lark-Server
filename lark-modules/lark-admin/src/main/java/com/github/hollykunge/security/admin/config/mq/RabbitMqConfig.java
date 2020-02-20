package com.github.hollykunge.security.admin.config.mq;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Notice;
import com.github.hollykunge.security.admin.mapper.NoticeMapper;
import com.github.hollykunge.security.common.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * RabbitMq配置
 * @author zhhongyu
 * @create 2019/5/9 13:37
 */
@Configuration
@Slf4j
public class RabbitMqConfig {
    @Resource
    private NoticeMapper noticeMapper;

    @Autowired
    private QueueConfig queueConfig;
    @Autowired
    private ExchangeConfig exchangeConfig;

    /**
     * 连接工厂
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     将消息队列和交换机进行绑定
     */
    @Bean
    @Order(value = 9)
    public Binding bindingPortal() {
        return BindingBuilder.bind(queueConfig.noticeQueue()).to(exchangeConfig.directExchange()).with(CommonConstants.NOTICE_TOPORTAL_ROTEING_KEY);
    }

    /**
     * 死信队列与死信交换机进行绑定
     * @return
     */
    @Bean
    @Order(value = 10)
    public Binding bindingDeadExchange() {
        return BindingBuilder.bind(queueConfig.noticeDeadQueue()).to(exchangeConfig.noticeDeadExchange()).with(AdminCommonConstant.DEAD_LETTER_ROUTING_KEY);
    }

    /**
     * 通知公告发送到研讨服务队列
     * @return
     */
    @Bean
    @Order(value = 11)
    public Binding bindingChat() {
        return BindingBuilder.bind(queueConfig.noticeToChatQueue()).to(exchangeConfig.directExchange()).with(CommonConstants.NOTICE_TOCHAT_ROTEING_KEY);
    }

    @Bean
    @Order(value = 12)
    public Binding bindingAdminUser() {
        return  BindingBuilder.bind(queueConfig.adminToUser()).to(exchangeConfig.adminDirectExchange()).with(CommonConstants.ADMIN_UNACK_USER_KEY);
    }

    @Bean
    @Order(value = 13)
    public Binding bindingAdminOrg() {
        return  BindingBuilder.bind(queueConfig.adminToOrg()).to(exchangeConfig.adminDirectExchange()).with(CommonConstants.ADMIN_UNACK_ORG_KEY);
    }

    /**
     * fansq
     * 绑定 cancelNoticeQueue noticeExchange
     * @return
     */
    @Bean
    @Order(value = 14)
    public Binding bindingCancelPortal() {
        return BindingBuilder.bind(queueConfig.cancelNoticeQueue()).to(exchangeConfig.directExchange()).with(CommonConstants.CANCEL_NOTICE_TOPORTAL_ROTEING_KEY);
    }
    /**
     * 定义rabbit template用于数据的接收和发送
     * @return
     */
    @Bean
    @Primary
    public RabbitTemplate noticeRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    Notice notice = new Notice();
                    notice.setIsSend("0");
                    notice.setId(correlationData.getId());
                    noticeMapper.updateByPrimaryKeySelective(notice);
                }
            }
        });
        return template;
    }

    @Bean
    @Qualifier("adminRabbitTemplate")
    public RabbitTemplate adminRabbitTemplate(){
        return  new RabbitTemplate(connectionFactory);
    }
}