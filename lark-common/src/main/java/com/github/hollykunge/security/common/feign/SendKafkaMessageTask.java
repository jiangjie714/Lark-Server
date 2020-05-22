package com.github.hollykunge.security.common.feign;


import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.log.dto.kafka.MessageDto;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 一个发送kafka错误日志的任务
 *
 * @author zhhongyu
 * @since 2020.5.19
 */
@Slf4j
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
        try{
            errorLogFeign.sendKafka(getTopicDto(errorMessageEntity));
        }catch (Exception e){
            log.error("调用日志服务采集错误日志接口失败");
            log.error("系统发生的错误为:{}",errorMessageEntity.toString());
        }
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
