package com.github.hollykunge.servicewebservice.config.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.vo.mq.AdminOrgVO;
import com.github.hollykunge.security.common.vo.mq.AdminUserVO;
import com.github.hollykunge.security.mq.constants.RabbitMqQueConstant;
import com.github.hollykunge.servicewebservice.dao.EryuanOrgDao;
import com.github.hollykunge.servicewebservice.dao.EryuanUserDao;
import com.github.hollykunge.servicewebservice.model.EryuanOrg;
import com.github.hollykunge.servicewebservice.model.EryuanUser;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author: yzq
 * @Date: 创建于 2019/7/25 15:24
 */
@Component
@Slf4j
public class AdminUnAckCosumer {
    @Autowired
    private EryuanOrgDao eryuanOrgDao;
    @Autowired
    private EryuanUserDao eryuanUserDao;

    @RabbitHandler
    @RabbitListener(queues = RabbitMqQueConstant.ADMIN_UNACK_ORG,containerFactory = "rabbitListenerContainerFactory")
    public void handleOrgMessage(Message message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        try {
            String json = new String(message.getBody(),"utf-8");
            List<AdminOrgVO> orgList = JSONArray.parseArray(json,AdminOrgVO.class);
            for (AdminOrgVO adminOrg:orgList) {
//                EryuanOrg eryuanOrg = new EryuanOrg();
//                Example example = new Example(EryuanOrg.class);
//                Example.Criteria criteria = example.createCriteria();
//                criteria.andEqualTo("casicOrgCode",adminOrg.getId());
//                eryuanOrg.setIsSuccess("0");
//                eryuanOrgDao.updateByExampleSelective(eryuanOrg,example);
                String isSuccess = "0";
                eryuanOrgDao.updateOrgIsSuccess(isSuccess,adminOrg.getId());
                log.info("admin未成功消费的orgQueue重发给web-webservice，已重置标志位。消息：" + adminOrg.toString());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("消费unAckOrgQueue失败，原因是：" + e.getMessage());
            e.printStackTrace();
        }
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag,false);
    }
    @RabbitHandler
    @RabbitListener(queues = RabbitMqQueConstant.ADMIN_UNACK_USER,containerFactory = "rabbitListenerContainerFactory")
    public void handleUserMessage(Message message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        try {
            String json = new String(message.getBody(),"utf-8");
            List<AdminUserVO> userList = JSONArray.parseArray(json,AdminUserVO.class);
            EryuanUser eryuanUser = new EryuanUser();
            for (AdminUserVO adminUser:userList) {
                eryuanUser.setIsSuccess("0");
                eryuanUser.setID(adminUser.getId());
                eryuanUserDao.updateByPrimaryKeySelective(eryuanUser);
                log.info("admin未成功消费的userQueue重发给web-webservice，已重置标志位。消息：" + adminUser.toString());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("消费unAckUserQueue失败，原因是：" + e.getMessage());
            e.printStackTrace();
        }
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag,false);
    }

    /**
     * 消费协同编辑队中组织消息
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitMqQueConstant.ONEDOC_ORG,containerFactory = "rabbitListenerContainerFactory")
    public void handleOnedocOrgMessage(Message message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        try {
            String json = new String(message.getBody(),"utf-8");
            List<AdminOrgVO> orgList = JSONArray.parseArray(json,AdminOrgVO.class);
            for (AdminOrgVO adminOrg:orgList) {
                String onedocIsSuccess = "0";
                eryuanOrgDao.updateOrgOnedocIsSuccess(onedocIsSuccess,adminOrg.getId());
                log.info("onedoc未成功消费的orgQueue重发给web-webservice，已重置标志位。消息：" + adminOrg.toString());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("消费onedoc_org失败，原因是：" + e.getMessage());
            e.printStackTrace();
        }
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag,false);
    }

    /**
     * 消费协同编辑队列中用户消息
     * @param message
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitMqQueConstant.ONEDOC_USER,containerFactory = "rabbitListenerContainerFactory")
    public void handleOnedocUserMessage(Message message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        try {
            String json = new String(message.getBody(),"utf-8");
            List<AdminUserVO> userList = JSONArray.parseArray(json,AdminUserVO.class);
            EryuanUser eryuanUser = new EryuanUser();
            for (AdminUserVO adminUser:userList) {
                eryuanUser.setOnedocIsSuccess("0");
                eryuanUser.setID(adminUser.getId());
                eryuanUserDao.updateByPrimaryKeySelective(eryuanUser);
                log.info("onedoc未成功消费的userQueue重发给web-webservice，已重置标志位。消息：" + adminUser.toString());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("消费onedoc_user失败，原因是：" + e.getMessage());
            e.printStackTrace();
        }
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag,false);
    }
}
