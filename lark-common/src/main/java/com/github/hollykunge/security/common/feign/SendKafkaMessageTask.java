package com.github.hollykunge.security.common.feign;


import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.log.dto.kafka.MessageDto;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;

/**
 * 一个发送kafka错误日志的任务
 *
 * @author zhhongyu
 * @since 2020.5.19
 */
public class SendKafkaMessageTask implements Runnable {
    private ErrorLogFeign errorLogFeign;
    private ErrorMessageEntity errorMessageEntity;
    /**
     * kafka配置到logstach的主题
     */
    private static final String KAFKATOIPIC = "system_error_topic";

    public SendKafkaMessageTask(ErrorLogFeign errorLogFeign, ErrorMessageEntity errorMessageEntity) {
        this.errorLogFeign = errorLogFeign;
        this.errorMessageEntity = errorMessageEntity;
    }

    @Override
    public void run() {
        errorLogFeign.sendKafka(getTopicDto(errorMessageEntity));
    }

    private TopicDto getTopicDto(ErrorMessageEntity errorMessageEntity) {
        TopicDto topicDto = new TopicDto();
        topicDto.setTopicName(KAFKATOIPIC);
        MessageDto messageDto = new MessageDto();
        //创建消息
        messageDto.setMessage(JSON.toJSONString(errorMessageEntity));
        topicDto.setMessage(messageDto);
        return topicDto;
    }
}
