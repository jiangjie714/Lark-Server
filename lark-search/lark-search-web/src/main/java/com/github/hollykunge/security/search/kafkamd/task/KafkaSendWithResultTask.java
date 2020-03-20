package com.github.hollykunge.security.search.kafkamd.task;

import org.springframework.kafka.support.SendResult;

import java.util.concurrent.Callable;

/**
 * 多线程发送kafka消息，有返回信息
 * @author zhhongyu
 */
public class KafkaSendWithResultTask extends AbstractTask implements Callable<SendResult> {
    private String topic;
    private String obj;
    public KafkaSendWithResultTask (String topic,String obj) {
        this.topic = topic;
        this.obj = obj;
    }

    @Override
    public SendResult call() throws Exception {
        toSend(topic,obj);
        return null;
    }
}
