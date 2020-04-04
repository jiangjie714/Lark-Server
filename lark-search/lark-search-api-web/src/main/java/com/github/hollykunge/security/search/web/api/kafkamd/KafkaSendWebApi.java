package com.github.hollykunge.security.search.web.api.kafkamd;

import com.github.hollykunge.security.search.dto.TopicDto;
import com.github.hollykunge.security.search.web.api.response.SearchObjectRestResponse;
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
    @RequestMapping(value = "/kafka/send",method = RequestMethod.POST)
    SearchObjectRestResponse sendKafka(TopicDto topic) throws Exception;
}
