package com.github.hollykunge.security.log.collect.kafka.rpc;

import com.alibaba.excel.util.StringUtils;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.log.collect.kafka.service.MessageSendService;
import com.github.hollykunge.security.log.collect.kafka.service.MongoDBService;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * kafka发送消息接口
 * @author zhoyou
 */
@RestController
@RequestMapping(value = "kafka")
public class KafkaSendController {

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
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Boolean> sendKafka(@RequestBody TopicDto topic) throws Exception{
        if(StringUtils.isEmpty(topic.getTopicName())){
            //todo 需要定义为feign调用参数异常
            throw new BaseException("主题名称不能为空...");
        }
        if(topic.getMessage() == null || StringUtils.isEmpty(topic.getMessage().getMessage())){
            //todo 需要定义为feign调用参数异常
            throw new BaseException("发送的消息不能为空...");
        }
        messageService.sendMessage(topic.getTopicName(),topic.getMessage().getMessage());
        return new ObjectRestResponse().data(true).rel(true);
    }
}
