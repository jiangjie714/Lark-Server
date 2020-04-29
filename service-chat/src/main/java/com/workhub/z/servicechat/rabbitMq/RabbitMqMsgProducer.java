package com.workhub.z.servicechat.rabbitMq;

import com.workhub.z.servicechat.config.RandomId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

//rabbit mq 发送消息
@Component
public class RabbitMqMsgProducer  implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    private RabbitTemplate rabbitTemplate;
    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public RabbitMqMsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }
    //传入object
    public void sendMsg(Object obj) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_CONTACT, RabbitConfig.ROUTINGKEY_CONTACT, obj, correlationId);
    }

    //群变动消息记录
    public void sendMsgGroupChange(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_CONTACT, RabbitConfig.ROUTINGKEY_GROUPCHANGE, obj, correlationId);
    }
    /**群(会议)审批日志消息记录*/
    public void sendMsgGroupApproveLog(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_CONTACT, RabbitConfig.ROUTINGKEY_GROUPAPPROVELOG, obj, correlationId);
    }

    /**socket 消息begin*/
    /*群体（群 会议 系统通知）消息*/
    public void sendSocketTeamMsg(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_SOCKET, RabbitConfig.ROUTINGKEY_SOCKET_TEAM_MSG, obj, correlationId);
    }
    /*私聊消息*/
    public void sendSocketPrivateMsg(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_SOCKET, RabbitConfig.ROUTINGKEY_SOCKET_PRIVATE_MSG, obj, correlationId);
    }
    /*绑定群体消息*/
    public void sendSocketTeamBindMsg(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_SOCKET, RabbitConfig.ROUTINGKEY_SOCKET_TEAM_BIND, obj, correlationId);
    }
    /*绑定群体列表消息*/
    public void sendSocketTeamListBindMsg(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_SOCKET, RabbitConfig.ROUTINGKEY_SOCKET_TEAMLIST_BIND, obj, correlationId);
    }
    /*解除绑定群体消息*/
    public void sendSocketTeamUnBindMsg(Object obj) {
        CorrelationData correlationId = new CorrelationData(RandomId.getUUID());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_SOCKET, RabbitConfig.ROUTINGKEY_SOCKET_TEAM_UNBIND, obj, correlationId);
    }
    /**socket 消息end*/

    /**
     * 消息是否到交换机中都有callback
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.warn("[唯一标识]  " + correlationData);
        if (ack) {
            logger.warn("[消息到交换机结果]  " + "成功");
        } else {
            logger.warn("[失败原因]  " + cause);
        }
    }

    /**
     * 消息没有到队列会调用该回调 (一般消息发送失败, 使用ReturnCallback就足够啦)
     */
    @Override
    public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.warn("[主体]  " + message);
        logger.warn("[replyCode]  " + replyCode);
        logger.warn("[描述]  " + replyText);
        logger.warn("[exchange]  " + exchange);
        logger.warn("[routingKey]  " + routingKey);
    }
}