package com.github.hollykunge.security.admin.config.mq;

import com.ace.cache.api.CacheAPI;
import com.ace.cache.service.ICacheManager;
import com.ace.cache.service.IRedisService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.util.MqSetBaseEntity;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.common.vo.mq.AdminOrgVO;
import com.github.hollykunge.security.mq.constants.RabbitMqQueConstant;
import com.github.hollykunge.security.mq.constants.RabbitMqRoutingKeyConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 组织数据变动消费者
 *
 * @author zhhongyu
 * @since 2019-07-23
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrgConsumer {
    @Resource
    private OrgMapper orgMapper;
    @Autowired
    private ProduceSenderConfig produceSenderConfig;
    @Autowired
    private CacheAPI cacheAPI;
    @Autowired
    private PlatformTransactionManager txManager;

    @RabbitHandler
    @RabbitListener(queues = RabbitMqQueConstant.ADMINORG_QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {

        String msg = new String(message.getBody());
        List<AdminOrgVO> adminOrgVOS = JSONArray.parseArray(msg, AdminOrgVO.class);
        List<AdminOrgVO> setMqOrgVO = new ArrayList<AdminOrgVO>();
        //开启事务
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        for (AdminOrgVO adminOrgVO :
                adminOrgVOS) {
            if (StringUtils.isEmpty(adminOrgVO.getOrgCode())) {
                log.error("mq中组织数据存在orgCode为空的数据...这样的数据属于错误数据，" +
                        "该变动组织信息已经从mq队列中剔除...其中包含如下数据："+adminOrgVO.toString());
                setMqOrgVO.add(adminOrgVO);
                continue;
            }
            Org org = new Org();
            org.setOrgCode(adminOrgVO.getOrgCode());
            Org exitOrg = orgMapper.selectOne(org);
            //1.数据库中不存在这个组织属于插入
            if (exitOrg == null) {
                exitOrg = JSONObject.parseObject(JSONObject.toJSONString(adminOrgVO), Org.class);
                MqSetBaseEntity.setCreatData(exitOrg);
                orgMapper.insertSelective(exitOrg);
                continue;
            }
            //2.如果数据库中含有信息属于变更组织信息
            String priKey = exitOrg.getId();
            BeanUtils.copyProperties(adminOrgVO, exitOrg);
            exitOrg.setId(priKey);
            MqSetBaseEntity.setUpdate(exitOrg);
            orgMapper.updateByPrimaryKeySelective(exitOrg);
        }
        try{
            cacheAPI.removeByPre("orgUsers");
            cacheAPI.removeByPre(AdminCommonConstant.CACHE_KEY_RPC_USER);
        }catch (Exception e){
            log.error("------>>>>>清除联系人缓存失败!!");
            log.error("------>>>>>清除人员信息缓存失败!!");
            log.warn("------->>>>>同步组织信息已经回滚!!!");
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            txManager.rollback(status);
            setMqOrgVO = adminOrgVOS;
        }
        if (setMqOrgVO.size() > 0) {
            //如果setMqOrgVO不为空，通知提供者，该批量数据含有OrgCode为空的数据
            String jsonStr = JSONObject.toJSONString(setMqOrgVO);
            Message noticeMessage = MessageBuilder.withBody(jsonStr.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setMessageId(UUID.randomUUID() + "").build();
            produceSenderConfig.adminUserOrOrgSend(noticeMessage, RabbitMqRoutingKeyConstant.ADMIN_UNACK_ORG_KEY);
        }
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag, false);
        //提交事务
        txManager.commit(status);
    }
}
