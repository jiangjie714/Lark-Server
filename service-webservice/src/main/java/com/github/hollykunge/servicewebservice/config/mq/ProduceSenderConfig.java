package com.github.hollykunge.servicewebservice.config.mq;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.mq.constants.RabbiMqExchangeConstant;
import com.github.hollykunge.security.mq.constants.RabbitMqRoutingKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author: yzq
 * @Date: 创建于 2019/7/23 17:34
 */
@Component
@Slf4j
public class ProduceSenderConfig {
    @Resource(name = "orgRabbitTemplate")
    private RabbitTemplate orgRabbitTemplate;
    @Resource(name = "userRabbitTemplate")
    private RabbitTemplate userRabbitTemplate;
    @Resource(name = "onedocOrgRabbitTemplate")
    private RabbitTemplate onedocOrgRabbitTemplate;
    @Resource(name = "onedocUserRabbitTemplate")
    private RabbitTemplate onedocUserRabbitTemplate;

    public void sendOrgList(String ids,List orgList){
        CorrelationData correlationData = new CorrelationData(ids);
        String json = JSONArray.toJSONString(orgList);
        Message message = MessageBuilder.withBody(json.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("gb2312")
                .build();
        log.info(new String(message.getBody()));
        orgRabbitTemplate.convertAndSend(RabbiMqExchangeConstant.WERSERVICE_ADMIN_USERANDORG_EXCHANGE, RabbitMqRoutingKeyConstant.ADMINORG_ROTEING_KEY,message,correlationData);
        log.info("org消息发送至admin:" + orgList.toString());
    }

    public void sendUserList(String ids,List userList){
        CorrelationData correlationData = new CorrelationData(ids);
        String json = JSONArray.toJSONString(userList);
        Message message = MessageBuilder.withBody(json.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("gb2312")
                .build();
        log.info(new String(message.getBody()));
        userRabbitTemplate.convertAndSend(RabbiMqExchangeConstant.WERSERVICE_ADMIN_USERANDORG_EXCHANGE,RabbitMqRoutingKeyConstant.ADMINUSER_ROTEING_KEY,message,correlationData);
        log.info("user消息发送至admin:" + userList.toString());
    }

    /**
     * 协同编辑组织推送
     * @param ids
     * @param orgList
     */
    public void sendOnedocOrgList(String ids,List orgList){
        CorrelationData correlationData = new CorrelationData(ids);
        String json = JSONArray.toJSONString(orgList);
        Message message = MessageBuilder.withBody(json.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("gb2312")
                .build();
        log.info(new String(message.getBody()));
        onedocOrgRabbitTemplate.convertAndSend(RabbiMqExchangeConstant.WERSERVICE_ADMIN_USERANDORG_EXCHANGE,RabbitMqRoutingKeyConstant.ADMINORG_ROTEING_KEY,message,correlationData);
        log.info("org消息发送至oneDoc:" + orgList.toString());
    }

    /**
     * 协同编辑用户推送
     * @param ids
     * @param userList
     */
    public void sendOnedocUserList(String ids,List userList){
        CorrelationData correlationData = new CorrelationData(ids);
        String json = JSONArray.toJSONString(userList);
        Message message = MessageBuilder.withBody(json.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("gb2312")
                .build();
        log.info(new String(message.getBody()));
        onedocUserRabbitTemplate.convertAndSend(RabbiMqExchangeConstant.WERSERVICE_ADMIN_USERANDORG_EXCHANGE,RabbitMqRoutingKeyConstant.ADMINUSER_ROTEING_KEY,message,correlationData);
        log.info("user消息发送至oneDoc:" + userList.toString());
    }
}
