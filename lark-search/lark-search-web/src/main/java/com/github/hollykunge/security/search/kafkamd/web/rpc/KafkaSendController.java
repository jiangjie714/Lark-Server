package com.github.hollykunge.security.search.kafkamd.web.rpc;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.search.dto.TopicDto;
import com.github.hollykunge.security.search.kafkamd.constants.KafkaConstants;
import com.github.hollykunge.security.search.kafkamd.entity.KafkaMessageEntity;
import com.github.hollykunge.security.search.kafkamd.service.KafkaService;
import com.github.hollykunge.security.search.kafkamd.service.MessageSendService;
import com.github.hollykunge.security.search.kafkamd.service.MongoDBService;
import com.github.hollykunge.security.search.kafkamd.service.impl.MongoDBServiceImpl;
import com.github.hollykunge.security.search.utils.SpringContextUtil;
import com.github.hollykunge.security.search.web.api.kafkamd.KafkaSendWebApi;
import com.github.hollykunge.security.search.web.api.response.SearchObjectRestResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka发送消息接口
 * @author zhoyou
 */
@RestController
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
    public SearchObjectRestResponse sendKafka(@RequestBody TopicDto topic) throws Exception{
        if(StringUtils.isEmpty(topic.getTopicName())){
            throw new BaseException("主题名称不能为空...");
        }
        if(topic.getMessage() == null || StringUtils.isEmpty(topic.getMessage().getMessage())){
            throw new BaseException("发送的消息不能为空...");
        }
        messageService.sendMessage(topic.getTopicName(),topic.getMessage().getMessage());
        return new SearchObjectRestResponse().data(true).rel(true);
    }
}
