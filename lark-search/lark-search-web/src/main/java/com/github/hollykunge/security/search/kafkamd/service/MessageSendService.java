package com.github.hollykunge.security.search.kafkamd.service;

/**
 * @author: zhhongyu
 * @description: 消息相关业务层接口
 * @since: Create in 9:47 2020/3/6
 */
public interface MessageSendService {
    /**
     * 对外提供的消息发送接口
     * @param topic 主题
     * @param obj jsonstring的消息体
     */
    void sendMessage(String topic, String obj) throws Exception;
}
