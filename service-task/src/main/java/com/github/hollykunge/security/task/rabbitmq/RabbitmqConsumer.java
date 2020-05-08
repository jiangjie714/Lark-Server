package com.github.hollykunge.security.task.rabbitmq;

import com.github.hollykunge.security.task.constant.TaskCommon;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation 消息队列消费者
 */
@Component
@RabbitListener(queues = TaskCommon.INVITEMEMBER_QUEUE,containerFactory = "rabbitListenerContainerFactory")
public class RabbitmqConsumer {

    /**
     * 邀请成员消息消费
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    public void handleMessage(LarkProject message, @Headers Map<String,Object> headers, Channel channel) throws Exception {

        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag,false);
    }
}
