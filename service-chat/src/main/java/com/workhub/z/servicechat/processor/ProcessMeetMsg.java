package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.ZzGroupMsg;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.workhub.z.servicechat.config.MessageType.MSG_ANSWER;
import static com.workhub.z.servicechat.config.VoToEntity.GroupMsgVOToModel;
import static com.workhub.z.servicechat.config.common.getJsonStringKeyValue;

@Service
public class ProcessMeetMsg extends AbstractMsgProcessor {

    public MsgSendStatusVo sendMsg(String msg, String ip) throws Exception {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();

        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String message = jsonObject.getString("data");
            ZzGroupMsg zzGroupMsg = (ZzGroupMsg)GroupMsgVOToModel(message);
            //判断涉密词汇begin
            MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
            //可以发送
            messageSecretValidVo.setSendStatus("1");
            String level = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.secretLevel"));
            String type = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.type"));

            String oId = common.nulToEmptyString(common.getJsonStringKeyValue(message,"id"));
            msgSendStatusVo.setOId(oId);
            if(type.equals("1")){
                //如果是文字信息
                //文字内容
                String title = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title"));
                messageSecretValidVo = super.messageSecretValid(title,level);
            }
            //判断涉密词汇end
            if(messageSecretValidVo.getSendStatus().equals("1")){
                //存储消息信息（新）
                String msgId = super.saveMessageInfo("MEET",ip,msg);
                msgSendStatusVo.setId(msgId);
                //把前端的消息id替换成后端的id
                String newMsg = common.setJsonStringKeyValue(msg,"data.id",msgId);
                //如果可以发送消息
                SocketMsgVo msgVo = new SocketMsgVo();
                msgVo.setCode(jsonObject.getString("code"));
                msgVo.setSender(zzGroupMsg.getMsgSender());
                msgVo.setReceiver(zzGroupMsg.getMsgReceiver());
                msgVo.setMsg(newMsg);
                //todo SocketMsgVo加密
                msgSendStatusVo.setMsg(msgVo);
                //todo 发消息后期改成前端连接信息中心
                //rabbitMqMsgProducer.sendSocketTeamMsg(msgVo);
            }else{
                msgSendStatusVo.setStatus(false);
                msgSendStatusVo.setContent("消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
                SocketMsgVo socketMsgVo = new SocketMsgVo();
                socketMsgVo.setCode(MSG_ANSWER+"");
                socketMsgVo.setSender((String)getJsonStringKeyValue(msg,"data.fromId"));
                socketMsgVo.setReceiver((String)getJsonStringKeyValue(msg,"data.fromId"));
                MsgAnswerVO answerVO = super.msgAnswer(msg,zzGroupMsg.getMsgId(), MessageType.FAIL_ANSWER,"消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
                socketMsgVo.setMsg(answerVO);
                //todo 发消息后期改成前端连接信息中心
                //rabbitMqMsgProducer.sendSocketMsgAnswer(socketMsgVo);
            }
        } catch (Exception e) {
            throw  e;
        }
        return msgSendStatusVo;
    }

    public MsgSendStatusVo meetUserChange(String userId, String msg) throws Exception {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();

        JSONObject jsonObject = JSONObject.parseObject(msg);
        String message = jsonObject.getString("data");
        String meetId = common.getJsonStringKeyValue(message,"id").toString();

        SocketMsgVo msgVo = new SocketMsgVo();
        //todo 改成socket代码规范
        msgVo.setCode(MessageType.SOCKET_TEAM_BIND);
        msgVo.setSender("");
        msgVo.setReceiver("");
        SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
        socketTeamBindVo.setTeamId(meetId);
        List userList = new ArrayList();
        userList.add(userId);
        socketTeamBindVo.setUserList(userList);
        msgVo.setMsg(socketTeamBindVo);
        //todo SocketMsgVo加密
        rabbitMqMsgProducer.sendSocketTeamBindMsg(msgVo);
        return msgSendStatusVo;
    }
}
