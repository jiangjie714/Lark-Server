package com.github.hollykunge.security.search.kafkamd.service;



/**
 * @author: zhhongyu
 * @description: kafka发送消息接口
 * @since: Create in 9:34 2020/3/4
 */
public interface KafkaService {
    /**
     * 发消息
     * @param topic 主题
     * @param obj 发送内容
     */
    void send(String topic, String obj);
}
