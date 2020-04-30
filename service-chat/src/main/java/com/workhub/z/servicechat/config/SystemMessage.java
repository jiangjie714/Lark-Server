package com.workhub.z.servicechat.config;

import com.workhub.z.servicechat.VO.CheckSocketMsgVo;
import com.workhub.z.servicechat.VO.SocketMsgDetailVo;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
*@Description: 处理所有消息，由系统统一发送消息到客户端
*@Author: 忠
*@date: 2019/10/14
*/
@Service
public class SystemMessage {
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    /**
      * 发送系统通知
      * @param socketTeamId socket的集合id，如群id、会议id、个人id等等
      * @param toFrontcontent 通知前端的内容
      */
    public void sendMessageToFront(String socketTeamId, SocketMsgDetailVo toFrontcontent){
        SocketMsgVo msgVo = new SocketMsgVo();
        msgVo.setCode(SocketMsgTypeEnum.TEAM_MSG);
        msgVo.setReceiver(socketTeamId);
        //msgVo.setMsg(JSON.toJSONString(toFrontcontent, SerializerFeature.DisableCircularReferenceDetect));
        msgVo.setMsg(toFrontcontent);
        //校验消息
        CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo);
        //只有消息合法才去绑定socket通信频道
        if(cRes.getRes()){
            rabbitMqMsgProducer.sendSocketTeamMsg(msgVo);
        }

    }

}
