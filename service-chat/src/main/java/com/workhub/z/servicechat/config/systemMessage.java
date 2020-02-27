package com.workhub.z.servicechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
*@Description: 处理所有消息，由系统统一发送消息到客户端
*@Author: 忠
*@date: 2019/10/14
*/
@Service
public class systemMessage {
    /**
    *@Description: 用于会议变更通知
    *@Param: content：消息内容，meetingId：会议id,userId：通知发起者
    *@return:
    *@Author: 忠
    *@date: 2019/10/14
    */
    public static void sendMessageToMeet(String meetingId,String userId,String content,RabbitMqMsgProducer rabbitMqMsgProducer){
        SocketMsgVo msgVo = new SocketMsgVo();
        msgVo.setCode(MessageType.SOCKET_MEET_CHANGE);
        msgVo.setReceiver(meetingId);
        msgVo.setMsg(content);
        rabbitMqMsgProducer.sendSocketTeamMsg(msgVo);
    }
    /**
      * 发送系统通知
      * @param socketTeamId socket的集合id，如群id、会议id、个人id等等
      * @param toFrontcontent 通知前端的内容
      */
    public static void sendMessageToFront(String socketTeamId,AnswerToFrontReponse toFrontcontent,RabbitMqMsgProducer rabbitMqMsgProducer){
        SocketMsgVo msgVo = new SocketMsgVo();
        msgVo.setCode(MessageType.SOCKET_SYSTEM);
        msgVo.setReceiver(socketTeamId);
        msgVo.setMsg(JSON.toJSONString(toFrontcontent, SerializerFeature.DisableCircularReferenceDetect));
        rabbitMqMsgProducer.sendSocketTeamMsg(msgVo);
    }

}
