package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.SocketMsgDetailTypeEnum;
import com.workhub.z.servicechat.config.SocketMsgTypeEnum;
import com.workhub.z.servicechat.entity.group.ZzGroupMsg;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.workhub.z.servicechat.config.Common.getJsonStringKeyValue;
import static com.workhub.z.servicechat.config.VoToEntity.GroupMsgVOToModel;

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
            String level = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.secretLevel"));
            String type = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.type"));

            String oId = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"id"));
            msgSendStatusVo.setOId(oId);
            if(type.equals("1")){
                //如果是文字信息
                //文字内容
                String title = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title"));
                messageSecretValidVo = super.messageSecretValid(title,level);
            }
            //判断涉密词汇end
            if(messageSecretValidVo.getSendStatus().equals("1")){
                //校验长度
                if(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title")).length()>300){
                    msgSendStatusVo.setStatus(false);
                    msgSendStatusVo.setContent("消息不能发送，超过最大字数限制300");
                    return msgSendStatusVo;
                }
                //存储消息信息（新）
                String msgId = super.saveMessageInfo("MEET",ip,msg);
                msgSendStatusVo.setId(msgId);
                //把前端的消息id替换成后端的id
                String newMsg = Common.setJsonStringKeyValue(msg,"data.id",msgId);
                //如果可以发送消息
                SocketMsgVo msgVo = new SocketMsgVo();
                msgVo.setCode(SocketMsgTypeEnum.TEAM_MSG);
                msgVo.setSender(zzGroupMsg.getMsgSender());
                msgVo.setReceiver(zzGroupMsg.getMsgReceiver());
                msgVo.setId(msgId);
                SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
                for(SocketMsgDetailTypeEnum senum:SocketMsgDetailTypeEnum.values()){
                    if(senum.getCode().equals(jsonObject.getString("code"))){
                        detailVo.setCode(senum);
                        break;
                    }

                }
                detailVo.setData(Common.getJsonStringKeyValue(newMsg,"data"));
                //msgVo.setMsg(newMsg);
                msgVo.setMsg(detailVo);
                //todo SocketDetailMsgVo加密
                msgSendStatusVo.setMsg(msgVo);
                //todo 发消息后期改成前端连接信息中心
                //todo 测试使用
                //rabbitMqMsgProducer.sendSocketTeamMsg(msgVo);
            }else{
                msgSendStatusVo.setStatus(false);
                msgSendStatusVo.setContent("消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
                SocketMsgVo socketMsgVo = new SocketMsgVo();
                socketMsgVo.setCode(SocketMsgTypeEnum.SINGLE_MSG);
                socketMsgVo.setSender((String)getJsonStringKeyValue(msg,"data.fromId"));
                socketMsgVo.setReceiver((String)getJsonStringKeyValue(msg,"data.fromId"));
                SocketMsgDetailVo answerVO = super.msgAnswer(msg,zzGroupMsg.getMsgId(), MessageType.FAIL_ANSWER,"消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
                socketMsgVo.setMsg(answerVO);
                //todo 发消息后期改成前端连接信息中心
                //rabbitMqMsgProducer.sendSocketPrivateMsg(socketMsgVo);
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
        String meetId = Common.getJsonStringKeyValue(message,"id").toString();

        SocketMsgVo msgVo = new SocketMsgVo();
        //todo 改成socket代码规范
        msgVo.setCode(SocketMsgTypeEnum.BIND_USER);
        msgVo.setSender("");
        msgVo.setReceiver("");
        SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
        socketTeamBindVo.setTeamId(meetId);
        List userList = new ArrayList();
        userList.add(userId);
        socketTeamBindVo.setUserList(userList);
        SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
        detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
        detailVo.setData(socketTeamBindVo);
        msgVo.setMsg(detailVo);
        //校验消息
        CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo);
        if(!cRes.getRes()){
            msgSendStatusVo.setStatus(false);
            msgSendStatusVo.setContent("群体绑定消息不合法");
            return msgSendStatusVo;
        }
        //todo SocketDetailMsgVo加密
        rabbitMqMsgProducer.sendSocketTeamBindMsg(msgVo);
        return msgSendStatusVo;
    }
}
