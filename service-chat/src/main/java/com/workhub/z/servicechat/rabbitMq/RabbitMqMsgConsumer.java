package com.workhub.z.servicechat.rabbitMq;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.vo.mq.MsgQueue;
import com.rabbitmq.client.Channel;
import com.workhub.z.servicechat.VO.GroupEditVO;
import com.workhub.z.servicechat.VO.SystemMsgVO;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.ZzGroupApproveLog;
import com.workhub.z.servicechat.entity.ZzGroupStatus;
import com.workhub.z.servicechat.model.UserListDto;
import com.workhub.z.servicechat.server.IworkServerConfig;
import com.workhub.z.servicechat.server.IworkWebsocketStarter;
import com.workhub.z.servicechat.service.ZzGroupApproveLogService;
import com.workhub.z.servicechat.service.ZzGroupStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;
import java.util.List;

import static com.workhub.z.servicechat.config.MessageType.*;


//系统消息队列
@Component
public class RabbitMqMsgConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ZzGroupStatusService zzGroupStatusService;
    @Autowired
    ZzGroupApproveLogService zzGroupApproveLogService;
    //一个生产者，一个消费者
    @RabbitListener(queues = RabbitConfig.QUEUE_SYSMSG)
    public void process( Message message, Channel channel) {
        logger.info("测试：接收处理队列A当中的消息");
        String msg =  new String(message.getBody());
        SystemMsgVO systemMsgVO = new SystemMsgVO();
        systemMsgVO.setCode(SYS_MSG);
        MsgQueue msgQueue = JSONObject.parseObject(msg,MsgQueue.class);
        WsServerStarter wsServerStarter = IworkWebsocketStarter.getWsServerStarter();
        // TODO: 2019/8/20 增加系统消息结构体，拼装成联系人，系统内所有用户均有系统消息好友
        boolean res = Tio.sendToUser(wsServerStarter.getServerGroupContext(),msgQueue.getMsgRec(),
                WsResponse.fromText(JSONObject.toJSONString(systemMsgVO), IworkServerConfig.CHARSET));
        try {
            // 消息删除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //模拟异常
            //int i=1/0;
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));//报错信息打印日志
            try {
                //发送异常消息回滚到队列，接着消费
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                logger.error(common.getExceptionMessage(ex));//报错信息打印日志
            }

        }

    }
    // 群（会议）编辑消息处理
    @RabbitListener(queues = RabbitConfig.QUEUE_GROUPEDIT)
    public void processGroupEdit( Message message, Channel channel) {

        String msg =  new String(message.getBody());
        GroupEditVO groupEditVO = JSONObject.parseObject(msg,GroupEditVO.class);
        WsServerStarter wsServerStarter = IworkWebsocketStarter.getWsServerStarter();
        List<UserListDto> userLists = groupEditVO.getData().getUserList();
        String groupId = groupEditVO.getData().getGroupId();
        switch (groupEditVO.getData().getType()){
            case GROUP_JOIN_MSG:
                for(UserListDto user:userLists){
                    ChannelContext channelContext = Tio.getChannelContextByBsId(IworkWebsocketStarter.getWsServerStarter().getServerGroupContext(),user.getUserId());
                    if (channelContext != null) {
                        Tio.bindGroup(channelContext, groupId);
                    }
                }
                break;
            case GROUP_EXIT_MSG:
                for(UserListDto user:userLists){
                    ChannelContext channelContext = Tio.getChannelContextByBsId(IworkWebsocketStarter.getWsServerStarter().getServerGroupContext(),user.getUserId());
                    if (channelContext != null) {
                        Tio.unbindGroup(groupId, channelContext);
                    }
                }
                break;
            default:
                break;
        }

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*boolean res = Tio.sendToUser(wsServerStarter.getServerGroupContext(),msgQueue.getMsgRec(),
                WsResponse.fromText(JSONObject.toJSONString(systemMsgVO), IworkServerConfig.CHARSET));
        try {
            // 消息删除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //模拟异常
            //int i=1/0;
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));//报错信息打印日志
            try {
                //发送异常消息回滚到队列，接着消费
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                logger.error(common.getExceptionMessage(ex));//报错信息打印日志
            }

        }*/

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
            logger.error("群/会议变更消息队列消费异常:"+common.getExceptionMessage(e));
            try {
                //发送异常消息回滚到队列，接着消费
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                //报错信息打印日志
                logger.error(common.getExceptionMessage(ex));
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
            logger.error("群/会议审批日志消息队列消费异常:"+common.getExceptionMessage(e));
            try {
                //发送异常消息回滚到队列，接着消费
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }catch (Exception ex){
                //报错信息打印日志
                logger.error(common.getExceptionMessage(ex));
            }

        }finally {
            // 消息删除，防止一条消息无限次消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
