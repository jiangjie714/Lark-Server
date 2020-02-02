package com.workhub.z.servicechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.workhub.z.servicechat.server.IworkServerConfig;
import com.workhub.z.servicechat.server.IworkWebsocketStarter;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.WsServerStarter;

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
    public static void sendMessageToMeet(String meetingId,String userId,String content){
        Tio.sendToGroup(IworkWebsocketStarter.getWsServerStarter().getServerGroupContext(),meetingId,WsResponse.fromText(content, IworkServerConfig.CHARSET));
    }
    /**
      * 发送系统通知
      * @param socketTeamId socket的集合id，如群id、会议id、个人id等等
      * @param toFrontcontent 通知前端的内容
      */
    public static void sendMessageToFront(String socketTeamId,AnswerToFrontReponse toFrontcontent){
        Tio.sendToGroup(IworkWebsocketStarter.getWsServerStarter().getServerGroupContext(),socketTeamId,WsResponse.fromText(JSON.toJSONString(toFrontcontent, SerializerFeature.DisableCircularReferenceDetect), IworkServerConfig.CHARSET));
    }

}
