package com.github.hollykunge.security.log.collect.kafka.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.log.collect.kafka.constants.KafkaConstants;
import com.github.hollykunge.security.log.collect.kafka.service.impl.MongoDBServiceImpl;
import com.github.hollykunge.security.log.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: kafka消息发送失败
 * @since: Create in 15:20 2020/3/6
 */
@Slf4j
@Component
public class KafkaSendListener implements ProducerListener {
    @Override
    public void onSuccess(String s, Integer integer, Object key, Object value, RecordMetadata recordMetadata) {
        log.info("发送kafka消息成功,,,,");
        Map<String,Object> mapmessage = JSONObject.parseObject((String) value,Map.class);
        String collectionName = (String) mapmessage.get(KafkaConstants.KAFKA_MONGODB_COLLECTION_NAME);
        Map<String,Object> id = new HashMap<>();
        id.put(KafkaConstants.KAFKA_SERVER_ID,mapmessage.get(KafkaConstants.KAFKA_SERVER_ID));
        try {
            SpringContextUtil.getBean(MongoDBServiceImpl.class).delete(id,collectionName);
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
    }

    @Override
    public void onError(String s, Integer integer, Object key, Object value, Exception e) {
        log.error("kafka发送消息失败---------->>>{}",value);
        log.error(ExceptionUtil.getMessage(e));
        //将mongodb中的数据改为0，标识这条消息发送失败
        Map<String,Object> mapmessage = JSONObject.parseObject((String)value,Map.class);
        String collectionName = (String) mapmessage.get(KafkaConstants.KAFKA_MONGODB_COLLECTION_NAME);
        Map<String,Object> id = new HashMap<>();
        id.put(KafkaConstants.KAFKA_SERVER_ID,mapmessage.get(KafkaConstants.KAFKA_SERVER_ID));
        Map<String,Object> contents = new HashMap<>();
        contents.put(KafkaConstants.KAFKA_SERVER_FALG,KafkaConstants.KAFKA_SERVER_DATA_INVALID);
        try {
            SpringContextUtil.getBean(MongoDBServiceImpl.class).updateFirst(id,contents,collectionName);
        } catch (Exception ex) {
            log.error(ExceptionCommonUtil.getExceptionMessage(ex));
        }

    }

    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }
}
