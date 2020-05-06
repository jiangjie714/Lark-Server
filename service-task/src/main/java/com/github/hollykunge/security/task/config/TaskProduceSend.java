package com.github.hollykunge.security.task.config;

import com.github.hollykunge.security.task.constant.TaskCommon;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation mq 消息发送工具类
 */
@Component
public class TaskProduceSend {

    @Resource(name = "taskRabbitTemplate")
    private RabbitTemplate taskRabbitTemplate;
    /**
     * fansq
     * @param id
     * @param message
     */
    public void sendCancelPortal(String id,Object message) {
        CorrelationData correlationId = new CorrelationData(id);
        //todo 暂定发送到交换机  后续在做具体消费
        taskRabbitTemplate.convertAndSend(TaskCommon.INVITEMEMBER_EXCHANGE, TaskCommon.INVITEMEMBER_DEAD_ROUTING_KEY,message, correlationId);
    }
}
