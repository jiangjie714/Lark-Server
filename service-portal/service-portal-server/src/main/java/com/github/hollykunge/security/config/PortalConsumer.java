package com.github.hollykunge.security.config;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.mq.HotMapVO;
import com.github.hollykunge.security.common.vo.mq.NoticeVO;
import com.github.hollykunge.security.common.vo.rpcvo.DiscussVO;
import com.github.hollykunge.security.common.vo.rpcvo.TaskVO;
import com.github.hollykunge.security.common.vo.rpcvo.TodoVO;
import com.github.hollykunge.security.constants.Constants;
import com.github.hollykunge.security.entity.HeatMap;
import com.github.hollykunge.security.entity.Notice;
import com.github.hollykunge.security.mapper.HeatMapMapper;
import com.github.hollykunge.security.mapper.NoticeMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 门户消息消费者,包含消费发布公告，消费工作热力图
 * @author zhhongyu
 * @create 2019/5/9 17:32
 */
@Component
@RabbitListener(queues = CommonConstants.NOTICE_QUEUE_NAMA,containerFactory = "rabbitListenerContainerFactory")
public class PortalConsumer {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private HeatMapMapper heatMapMapper;

    /**
     * 工作热力图消费处理
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    public void handleMessage(HotMapVO message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        // 处理消息
        HeatMap heatMap = new HeatMap();
        if(StringUtils.isEmpty(message.getUserId())){
            throw new BaseException("消费工作热力图消息，用户id不能为空...");
        }
        heatMap.setUserId(message.getUserId());
        heatMap.setMapDate(message.getMapDate());
        heatMap = heatMapMapper.selectOne(heatMap);
        int count;
        if(heatMap == null){
            heatMap = new HeatMap();
            BeanUtils.copyProperties(message,heatMap);
            heatMap.setCommits(0);
            heatMap.setId(UUIDUtils.generateShortUuid());
            count = heatMapMapper.insertSelective(heatMap);
        }else{
            heatMap.setCommits(heatMap.getCommits()+1);
             count = heatMapMapper.updateByPrimaryKeySelective(heatMap);
        }
        if(count>0){
            //手动ack
            long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //手动签收消息
            channel.basicAck(deliveryTag,false);
        }
    }

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
        notice.setId(UUIDUtils.generateShortUuid());
        int count = noticeMapper.insertSelective(notice);
        if(count>0){
            //手动ack
            long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //手动签收消息
            channel.basicAck(deliveryTag,false);
        }
    }
}