package com.github.hollykunge.security.config;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.rpcvo.ContactVO;
import com.github.hollykunge.security.common.vo.rpcvo.MessageContent;
import com.github.hollykunge.security.entity.Message;
import com.github.hollykunge.security.mapper.MessageMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 门户message消息消费者
 * @author zhhongyu
 */
@Component
@Slf4j
public class MessageConsumer {
    @Autowired
    private MessageMapper messageMapper;
    @RabbitHandler
    @RabbitListener(queues = "queue_contact",containerFactory = "simpleRabbitListenerContainerFactory")
    public void handleMessage(Map<String,List<ContactVO>> contactMap, @Headers Map<String,Object> headers, Channel channel) throws Exception {
            Set<String> sets = contactMap.keySet();
            List<ContactVO> data = null;
            String userId = "";
            for (String temp : sets) {
                userId = temp;
                data = contactMap.get(userId);
            }
            String finalUserId = userId;
            data.stream().forEach(contactVO -> {
                Message message = new Message();
                message.setUserId(finalUserId);
                this.transferData(contactVO, message);
                messageMapper.insertSelective(message);
            });
            //手动ack
            long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //手动签收消息
            channel.basicAck(deliveryTag,false);
    }

    private void transferData(ContactVO contactVO,Message messageEntity){
        messageEntity.setId(UUIDUtils.generateShortUuid());
        messageEntity.setAvatar(contactVO.getAvatar());
        messageEntity.setFromUsername(contactVO.getName());
        messageEntity.setMsgSender(contactVO.getName());
        messageEntity.setSendTime(this.stringToDate(contactVO.getFullTime(),"yyyy-MM-dd HH:mm:ss"));
        MessageContent lastMessage = contactVO.getLastMessage();
        if(lastMessage == null){
            throw new BaseException("消费消息中没有最后一条消息实体类");
        }
        messageEntity.setLevels(lastMessage.getSecretLevel()+"");
        messageEntity.setMsg(lastMessage.getTitle());
        messageEntity.setMsgType(lastMessage.getType()+"");
        messageEntity.setRead("0");
        messageEntity.setSendUserId(contactVO.getId());
    }

    private Date stringToDate(String source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (Exception e) {
        }
        return date;
    }
}
