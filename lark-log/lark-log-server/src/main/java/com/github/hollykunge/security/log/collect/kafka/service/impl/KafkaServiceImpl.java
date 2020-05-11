package com.github.hollykunge.security.log.collect.kafka.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.log.collect.kafka.constants.KafkaConstants;
import com.github.hollykunge.security.log.collect.kafka.service.KafkaService;
import com.github.hollykunge.security.log.collect.kafka.service.MongoDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: zhhongyu
 * @description: kafka业务实现类
 * @since: Create in 9:36 2020/3/4
 */
@Slf4j
@Service("kafkaService")
public class KafkaServiceImpl implements KafkaService {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private MongoDBService mongoDBService;

    @Override
    public void send(String topic, String obj){
        Map<String, Object> message = JSONObject.parseObject(obj, Map.class);
        message.put(KafkaConstants.KAFKA_SERVER_ID, UUIDUtils.generateShortUuid());
        message.put(KafkaConstants.KAFKA_SERVER_FALG, KafkaConstants.KAFKA_SERVER_DATA_EFFECTIVE);
        message.put(KafkaConstants.KAFKA_MONGODB_COLLECTION_NAME, topic);
        try {
            //将消息保存到mongodb,创建索引
            mongoDBService.insert(message, topic);
            mongoDBService.createIndex(topic, KafkaConstants.KAFKA_SERVER_ID);
        } catch (Exception e) {
            log.error("存储mongodb消息失败...");
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
        kafkaTemplate.send(topic,message.get(KafkaConstants.KAFKA_SERVER_ID) ,JSONObject.toJSONString(message));
    }
}
