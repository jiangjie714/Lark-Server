package com.workhub.z.servicechat.rabbitMq;

import com.github.hollykunge.security.common.vo.mq.NoticeVO;
import com.rabbitmq.client.Channel;
import com.workhub.z.servicechat.config.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;


//消费ace的新消息通知（用来标记客户端右上角的铃铛红点）
@Component
public class NoticeRabbitMqConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitListener(queues = "${aceNoticeRabbitMqQueueName}")
    public void handleMessage(NoticeVO notice, @Headers Map<String,Object> headers, Channel channel) {
        logger.info("消费来自ace通知新消息的mq:"+notice.getId());
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
             //TODO 业务代码待处理
            //手动签收消息
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));//报错信息打印日志
        }
    }
}
