package com.github.hollykunge.security.log.collect.kafka.task;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 无返回值的kafka发送消息
 */
@Slf4j
public class KafkaSendWithoutResultTask extends AbstractTask implements Runnable {
    private String topic;
    private String obj;
    public KafkaSendWithoutResultTask (String topic,String obj) {
        this.topic = topic;
        this.obj = obj;
    }
    @Override
    public void run() {
            toSend(topic,obj);
    }
}
