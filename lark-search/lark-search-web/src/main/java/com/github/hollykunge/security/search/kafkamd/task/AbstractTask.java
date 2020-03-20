package com.github.hollykunge.security.search.kafkamd.task;

import com.github.hollykunge.security.search.kafkamd.service.KafkaService;
import com.github.hollykunge.security.search.utils.SpringContextUtil;
import org.springframework.kafka.support.SendResult;

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
