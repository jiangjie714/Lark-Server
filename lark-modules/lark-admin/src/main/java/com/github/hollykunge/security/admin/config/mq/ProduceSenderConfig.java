package com.github.hollykunge.security.admin.config.mq;

import com.github.hollykunge.security.common.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 消息发送  生产者1
 * @author zhhongyu
 * @create 2019/5/9 14:28
 */
@Slf4j
@Component
public class ProduceSenderConfig{

    @Resource(name = "noticeRabbitTemplate")
    private RabbitTemplate noticRabbitTemplate;

    @Resource(name = "adminRabbitTemplate")
    private RabbitTemplate adminRabbitTemplate;

    /**
     * 发送消息,使用发送消息mq确认机制
     * @param message  消息
     */
    public void send(String id,Object message) {
        //消息id
        CorrelationData correlationId = new CorrelationData(id);
        //发送到门户服务
        noticRabbitTemplate.convertAndSend(CommonConstants.NOTICE_EXCHANGE, CommonConstants.NOTICE_TOPORTAL_ROTEING_KEY,message, correlationId);
        //发送到研讨服务
        noticRabbitTemplate.convertAndSend(CommonConstants.NOTICE_EXCHANGE, CommonConstants.NOTICE_TOCHAT_ROTEING_KEY,message, correlationId);
    }

    /**
     * 发送消息，没有确认机制
     * @param id
     * @param message
     */
    public void sendAndNoConfirm(String id,Object message) {
        //消息id
        CorrelationData correlationId = new CorrelationData(id);
        adminRabbitTemplate.convertAndSend(CommonConstants.NOTICE_EXCHANGE, CommonConstants.NOTICE_TOPORTAL_ROTEING_KEY,message, correlationId);
    }

    /**
     * 消息提供者可能出现mq服务上面的消息没有身份信息（user没有提供身份证号码，org没有提供orgCode）
     * admin服务消费消息时给消息提供者一个回执，批量未被成功消费的消息
     * @param message 消息体
     * @param rotingKey 路由键
     */
    public void adminUserOrOrgSend(Message message,String rotingKey) {
        adminRabbitTemplate.convertAndSend(CommonConstants.ADMIN_USERORORG_EXCHANGE, rotingKey,message);
    }
}