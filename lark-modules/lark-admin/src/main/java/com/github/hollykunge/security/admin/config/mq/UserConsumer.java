package com.github.hollykunge.security.admin.config.mq;

import com.ace.cache.annotation.CacheClear;
import com.ace.cache.api.CacheAPI;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.PositionUserMap;
import com.github.hollykunge.security.admin.entity.RoleUserMap;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.feign.RandomSelectDefaultAvatorFeign;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.util.MqSetBaseEntity;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.feign.LarkFeignFactory;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.common.vo.mq.AdminUserVO;
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
 * 人员数据消费者
 *
 * @author zhhongyu
 */
@Component
@Slf4j
public class UserConsumer {
    @Autowired
    public UserConsumer(RandomSelectDefaultAvatorFeign randomSelectDefaultAvatorFeign){
        this.randomSelectDefaultAvatorFeign = LarkFeignFactory.getInstance().loadFeign(randomSelectDefaultAvatorFeign);
    }
    @Resource
    private UserMapper userMapper;

    @Autowired
    private ProduceSenderConfig produceSenderConfig;

    @Resource
    private PositionUserMapMapper positionUserMapMapper;

    @Resource
    private RoleUserMapMapper roleUserMapMapper;

    private RandomSelectDefaultAvatorFeign randomSelectDefaultAvatorFeign;
    @Autowired
    private CacheAPI cacheAPI;

    @Autowired
    private PlatformTransactionManager txManager;

    @RabbitHandler
    @RabbitListener(queues = RabbitMqQueConstant.ADMINUSER_QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {

        String msg = new String(message.getBody());
        log.info("消费人员信息为：{}", msg);
        List<AdminUserVO> adminUserVOS = JSONArray.parseArray(msg, AdminUserVO.class);
        List<AdminUserVO> setMqUserVO = new ArrayList<AdminUserVO>();
        //开启事务
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        for (AdminUserVO adminUserVO :
                adminUserVOS) {
            if (StringUtils.isEmpty(adminUserVO.getPId())) {
                log.error("mq同步人员存在人员没有身份证号的信息集...该条mq消息属于错误数据，" +
                        "已经从mq队列中剔除,其中包含如下数据:" + adminUserVO.toString());
                setMqUserVO.add(adminUserVO);
                continue;
            }
            User user = new User();
            user.setPId(adminUserVO.getPId());
            User existUser = userMapper.selectOne(user);
            RoleUserMap roleUserMap = new RoleUserMap();
            PositionUserMap positionUserMap0 = new PositionUserMap();
            PositionUserMap positionUserMap1 = new PositionUserMap();
            PositionUserMap positionUserMap2 = new PositionUserMap();
            //数据库中没有改人员信息，属于新增
            if (existUser == null) {
                existUser = new User();
                existUser = JSONObject.parseObject(JSONObject.toJSONString(adminUserVO), User.class);
                MqSetBaseEntity.setCreatData(existUser);
                try {
                    ObjectRestResponse<FileInfoVO> objectRestResponse = randomSelectDefaultAvatorFeign.randomSelectDefaultAvator();
                    FileInfoVO result = objectRestResponse.getResult();
                    existUser.setAvatar(result.getFullPath());
                    userMapper.insertSelective(existUser);
                    //添加普通用户角色
                    roleUserMap.setId(UUIDUtils.generateShortUuid());
                    roleUserMap.setUserId(existUser.getId());
                    roleUserMap.setRoleId(AdminCommonConstant.USER_ROLE_ORDINARY);
                    MqSetBaseEntity.setCreatData(roleUserMap);
                    roleUserMapMapper.insertSelective(roleUserMap);
                    //添加可建群岗位
                    positionUserMap0.setId(UUIDUtils.generateShortUuid());
                    positionUserMap0.setUserId(existUser.getId());
                    positionUserMap0.setPositionId(AdminCommonConstant.USER_POSITION_ROOM_INNER);
                    positionUserMap1.setId(UUIDUtils.generateShortUuid());
                    positionUserMap1.setUserId(existUser.getId());
                    positionUserMap1.setPositionId(AdminCommonConstant.USER_POSTTION_ROOM_OUTTER);
                    positionUserMap2.setId(UUIDUtils.generateShortUuid());
                    positionUserMap2.setUserId(existUser.getId());
                    positionUserMap2.setPositionId(AdminCommonConstant.USER_POSITION_INSTITUTES_OUTTER);
                    MqSetBaseEntity.setCreatData(positionUserMap0);
                    MqSetBaseEntity.setCreatData(positionUserMap1);
                    MqSetBaseEntity.setCreatData(positionUserMap2);
                    positionUserMapMapper.insertSelective(positionUserMap0);
                    positionUserMapMapper.insertSelective(positionUserMap1);
                    positionUserMapMapper.insertSelective(positionUserMap2);
                    continue;
                } catch (Exception e) {
                    setMqUserVO.add(adminUserVO);
                    log.error(ExceptionCommonUtil.getExceptionMessage(e));
                    log.info("新增人员{},由于服务错误原因，已经返回到mq服务...", adminUserVO.getPId());
                    //回滚事务
                    txManager.rollback(status);
                    continue;
                }
            }
            //数据库中含有人员信息，属于修改
            String priKey = existUser.getId();
            BeanUtils.copyProperties(adminUserVO, existUser);
            existUser.setId(priKey);
            MqSetBaseEntity.setUpdate(existUser);
            try {
                userMapper.updateByPrimaryKeySelective(existUser);
                String[] clearKeys = new String[]{AdminCommonConstant.CACHE_KEY_RPC_USER+":" + existUser.getId(),
                        AdminCommonConstant.CACHE_KEY_RPC_USER +":"+ existUser.getPId()
                };
                cacheAPI.remove(clearKeys);
            } catch (Exception e) {
                setMqUserVO.add(adminUserVO);
                log.error(ExceptionCommonUtil.getExceptionMessage(e));
                log.info("新增人员{},由于服务错误原因，已经返回到mq服务...", adminUserVO.getPId());
                //回滚事务
                txManager.rollback(status);
            }
        }
        try{
            cacheAPI.removeByPre("orgUsers");
        }catch (Exception e){
            log.error("------>>>>>清除联系人缓存失败!!");
            log.warn("------->>>>>同步组织信息已经回滚!!!");
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            setMqUserVO = adminUserVOS;
            //回滚事务
            txManager.rollback(status);
        }
        //如果setMqUserVO不是空数组，重新发送一个队列，这些数据为未被消费的错误数据信息集
        if (setMqUserVO.size() > 0) {
            //如果setMqUserVO不为空，通知提供者，该批量数据含有身份证号为空的数据。
            String jsonStr = JSONObject.toJSONString(setMqUserVO);
            Message noticeMessage = MessageBuilder.withBody(jsonStr.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setMessageId(UUID.randomUUID() + "").build();
            produceSenderConfig.adminUserOrOrgSend(noticeMessage, RabbitMqRoutingKeyConstant.ADMIN_UNACK_USER_KEY);
        }
        //手动ack
        long deliveryTag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动签收消息
        channel.basicAck(deliveryTag, false);
        //提交事务
        txManager.commit(status);
    }
}
