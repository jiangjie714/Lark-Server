package com.workhub.z.servicechat.rabbitMq;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.vo.mq.MsgQueue;
import com.rabbitmq.client.Channel;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.SocketMsgDetailTypeEnum;
import com.workhub.z.servicechat.config.SocketMsgTypeEnum;
import com.workhub.z.servicechat.entity.group.ZzGroupApproveLog;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.service.ZzGroupApproveLogService;
import com.workhub.z.servicechat.service.ZzGroupStatusService;
import com.workhub.z.servicechat.service.ZzMeetingUserService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.workhub.z.servicechat.config.MessageType.SYS_MSG;


//系统消息队列
@Component
public class RabbitMqMsgConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ZzGroupStatusService zzGroupStatusService;
    @Autowired
    ZzGroupApproveLogService zzGroupApproveLogService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    @Autowired
    ZzUserGroupService zzUserGroupService;
    @Autowired
    ZzMeetingUserService zzMeetingUserService;
    //一个生产者，一个消费者
    @RabbitListener(queues = RabbitConfig.QUEUE_SYSMSG)
    public void process( Message message, Channel channel) {
        logger.info("测试：接收处理队列A当中的消息");
        String msg =  new String(message.getBody());
        SystemMsgVo systemMsgVO = new SystemMsgVo();
        systemMsgVO.setCode(SYS_MSG);
        MsgQueue msgQueue = JSONObject.parseObject(msg,MsgQueue.class);
        //WsServerStarter wsServerStarter = IworkWebsocketStarter.getWsServerStarter();
        // TODO: 2019/8/20 增加系统消息结构体，拼装成联系人，系统内所有用户均有系统消息好友
        /*boolean res = Tio.sendToUser(wsServerStarter.getServerGroupContext(),msgQueue.getMsgRec(),
                WsResponse.fromText(JSONObject.toJSONString(systemMsgVO), IworkServerConfig.CHARSET));*/
        try {
            // 消息删除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //模拟异常
            //int i=1/0;
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));//报错信息打印日志
            try {
                //发送异常消息回滚到队列，接着消费
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                logger.error(Common.getExceptionMessage(ex));//报错信息打印日志
            }

        }

    }
    /**todo
     * 加入重试机制 和 死信队列处理
     */
    /** 群（会议）变更消息记录*/
    @RabbitListener(queues = RabbitConfig.QUEUE_GROUPCHANGE)
    public void processGroupChange( Message message, Channel channel) throws Exception{

        try {
           // String msg =  new String(message.getBody());
            String msg =  new String(message.getBody(),"utf-8");
            ZzGroupStatus zzGroupStatus = JSONObject.parseObject(msg,ZzGroupStatus.class);
            logger.info("接收到群/会议变更消息："+JSONObject.toJSONString(zzGroupStatus));
            /**默认群会议*/
            if(zzGroupStatus.getType()==null || "".equals(zzGroupStatus.getType())){
                String groupType = MessageType.FLOW_LOG_GROUP;
                zzGroupStatus.setType(groupType);
            }
            zzGroupStatusService.add(zzGroupStatus);

            //模拟异
            //int i=1/0;
        }catch (Exception e){
            /**报错信息打印日志*/
            logger.error("群/会议变更消息队列消费异常:"+ Common.getExceptionMessage(e));
            try {
                //发送异常消息回滚到队列，接着消费
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                //报错信息打印日志
                logger.error(Common.getExceptionMessage(ex));
            }

        }finally {
            // 消息删除，防止一条消息无限次消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
    /**todo
     * 加入重试机制 和 死信队列处理
     */
    /** 群（会议）审批日志记录*/
    @RabbitListener(queues = RabbitConfig.QUEUE_GROUP_APPROVELOG)
    public void processGroupApproveLog( Message message, Channel channel) throws Exception{

        try {
            // String msg =  new String(message.getBody());
            String msg =  new String(message.getBody(),"utf-8");
            ZzGroupApproveLog zzGroupApproveLog = JSONObject.parseObject(msg,ZzGroupApproveLog.class);
            logger.info("接收到群/会议审批日志消息："+JSONObject.toJSONString(zzGroupApproveLog));
            /**默认群会议*/
            if(zzGroupApproveLog.getType()==null || "".equals(zzGroupApproveLog.getType())){
                zzGroupApproveLog.setType(MessageType.FLOW_LOG_GROUP);
            }
            zzGroupApproveLogService.add(zzGroupApproveLog);

            //模拟异
            //int i=1/0;
        }catch (Exception e){
            /**报错信息打印日志*/
            logger.error("群/会议审批日志消息队列消费异常:"+ Common.getExceptionMessage(e));
            try {
                //发送异常消息回滚到队列，接着消费
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                //报错信息打印日志
                logger.error(Common.getExceptionMessage(ex));
            }

        }finally {
            // 消息删除，防止一条消息无限次消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
    //进行人员群体信息绑定（发消息给socket）
    @RabbitListener(queuesToDeclare = @Queue(RabbitConfig.QUEUE_CHAT_USER_TEAM_BIND))
    public void dealBindUserTeamInf(Message message,Channel channel){
        try{
            String msg = new String(message.getBody(),"utf-8");
            SocketMsgVo vo = JSONObject.parseObject(msg,SocketMsgVo.class);

            String userId = vo.getMsg().getData().toString();
            List<String> groupList = zzUserGroupService.getGroupByUserId(userId);
            groupList.addAll(zzMeetingUserService.getMeetingByUserId(userId));
            SocketMsgVo vo2 = new SocketMsgVo();
            vo2.setCode(SocketMsgTypeEnum.BIND_LIST);

            SocketTeamListBindVo socketTeamListBindVo = new SocketTeamListBindVo();
            socketTeamListBindVo.setTeamList(groupList);
            socketTeamListBindVo.setUserId(userId);
            SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
            detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
            detailVo.setData(socketTeamListBindVo);
            vo2.setMsg(detailVo);
            //如果校验通过才能转发
            if(Common.checkSocketMsg(vo).getRes()){
                rabbitMqMsgProducer.sendSocketTeamListBindMsg(vo2);
            }
        }catch (Exception e){
            logger.error("人员群体信息绑定消息处理报错");
            logger.error(Common.getExceptionMessage(e));
        }finally {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                logger.error("人员群体信息绑定消息删除报错");
                logger.error(Common.getExceptionMessage(e));
            }
        }
    }
}
