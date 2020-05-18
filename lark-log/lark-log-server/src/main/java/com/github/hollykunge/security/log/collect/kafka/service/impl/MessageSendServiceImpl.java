package com.github.hollykunge.security.log.collect.kafka.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.log.collect.kafka.handler.KafkaSendHandler;
import com.github.hollykunge.security.log.collect.kafka.service.MessageSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.LogException;
import org.springframework.stereotype.Service;

/**
 * @author: zhhongyu
 * @description: 消息发送业务接口实现
 * @since: Create in 9:48 2020/3/6
 */
@Service
@Slf4j
public class MessageSendServiceImpl implements MessageSendService {
    @Override
    public void sendMessage(String topic, String obj)throws Exception{
        if(StringUtils.isEmpty(topic) || StringUtils.isEmpty(obj)){
            throw new LogException("主题或者消息体内容不能为空...");
        }
        try{
            KafkaSendHandler instance = KafkaSendHandler.getInstance();
            instance.pushDataToQueue(topic,obj);
        }catch (Exception e){
            log.error("kafka消息推送到队列失败...");
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            throw e;
        }
    }
}