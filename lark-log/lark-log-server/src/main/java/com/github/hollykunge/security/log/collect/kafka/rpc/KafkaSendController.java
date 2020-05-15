package com.github.hollykunge.security.log.collect.kafka.rpc;

import com.alibaba.excel.util.StringUtils;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.log.api.dto.TopicDto;
import com.github.hollykunge.security.log.api.kafka.KafkaSendWebApi;
import com.github.hollykunge.security.log.api.response.LogObjectRestResponse;
import com.github.hollykunge.security.log.collect.kafka.service.MessageSendService;
import com.github.hollykunge.security.log.collect.kafka.service.MongoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * kafka发送消息接口
 * @author zhoyou
 */
@RestController
@RequestMapping(value = "kafka")
public class KafkaSendController implements KafkaSendWebApi {

    @Autowired
    private MessageSendService messageService;
    @Autowired
    private MongoDBService mongoDBService;

    /**
     * 发送kafka消息
     * @param topic
     * @return
     * @throws Exception
     */
    @Override
    public LogObjectRestResponse sendKafka(@RequestBody TopicDto topic) throws Exception{
        if(StringUtils.isEmpty(topic.getTopicName())){
            throw new BaseException("主题名称不能为空...");
        }
        if(topic.getMessage() == null || StringUtils.isEmpty(topic.getMessage().getMessage())){
            throw new BaseException("发送的消息不能为空...");
        }
        messageService.sendMessage(topic.getTopicName(),topic.getMessage().getMessage());
        return new LogObjectRestResponse().data(true).rel(true);
    }
}
