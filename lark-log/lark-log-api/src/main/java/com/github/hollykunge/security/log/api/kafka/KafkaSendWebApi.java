package com.github.hollykunge.security.log.api.kafka;


import com.github.hollykunge.security.log.api.dto.TopicDto;
import com.github.hollykunge.security.log.api.response.LogObjectRestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: zhhongyu
 * @description: kafka消息发送接口
 * @since: Create in 10:44 2020/3/6
 */
public interface KafkaSendWebApi {
    /**
     * 发送kafka消息
     * @param topic
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    LogObjectRestResponse sendKafka(TopicDto topic) throws Exception;
}
