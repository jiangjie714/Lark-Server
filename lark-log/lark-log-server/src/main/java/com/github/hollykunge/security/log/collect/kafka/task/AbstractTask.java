package com.github.hollykunge.security.log.collect.kafka.task;


import com.github.hollykunge.security.log.collect.kafka.service.KafkaService;
import com.github.hollykunge.security.log.utils.SpringContextUtil;

/**
 * 任务抽象类
 * @author zhhongyu
 */
public abstract class AbstractTask {

    /**
     * 公共发送方法
     *
     * @param topic 主题名称/mongodb集合
     * @param obj
     */
    void toSend(String topic, String obj) {
        SpringContextUtil.getBean(KafkaService.class).send(topic,obj);
    }
}
