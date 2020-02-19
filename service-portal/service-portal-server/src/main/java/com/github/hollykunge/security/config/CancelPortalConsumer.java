package com.github.hollykunge.security.config;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.mq.HotMapVO;
import com.github.hollykunge.security.common.vo.mq.NoticeVO;
import com.github.hollykunge.security.entity.HeatMap;
import com.github.hollykunge.security.entity.Notice;
import com.github.hollykunge.security.mapper.HeatMapMapper;
import com.github.hollykunge.security.mapper.NoticeMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Map;

/**
 * @author fansq
 * @create 20-2-18
 * 消费 取消公告发布的消息
 */
@Component
@RabbitListener(queues = CommonConstants.CANCEL_NOTICE_QUEUE_NAME,containerFactory = "rabbitListenerContainerFactory")
public class CancelPortalConsumer {
    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 公告消息消费
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    public void handleMessage(NoticeVO message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        // 处理消息
        Notice notice = new Notice();
        BeanUtils.copyProperties(message,notice);
        Example example = new Example(Notice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fromId",notice.getFromId());
        int count = noticeMapper.deleteByExample(example);
        if(count>0){
            //手动ack
            long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //手动签收消息
            channel.basicAck(deliveryTag,false);
        }
    }
}