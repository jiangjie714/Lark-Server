package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.VO.MsgAnswerVO;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.config.CacheConst;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.ZzPrivateMsg;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.workhub.z.servicechat.config.MessageType.MSG_ANSWER;
import static com.workhub.z.servicechat.config.VoToEntity.MsgVOToModel;
import static com.workhub.z.servicechat.config.common.getJsonStringKeyValue;

@Service
public class ProcessPrivateMsg extends AbstractMsgProcessor{

    @Autowired
    protected ZzPrivateMsgService privateMsgService;

    public MsgSendStatusVo sendMsg(String msg, String ip) throws Exception {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();
        JSONObject jsonObject = JSONObject.parseObject(msg);

//      String code = jsonObject.getString("code");
        String message = jsonObject.getString("data");
        //判断涉密词汇begin
        MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
        messageSecretValidVo.setSendStatus("1");//可以发送
        String level = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.secretLevel"));
        String type = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.type"));

        String oId = common.nulToEmptyString(common.getJsonStringKeyValue(message,"id"));
        msgSendStatusVo.setOId(oId);
        if(type.equals("1")){//如果是文字信息
            //文字内容
            String title = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title"));
            messageSecretValidVo = super.messageSecretValid(title,level);
        }
        //判断涉密词汇end
        ZzPrivateMsg privateMsg = (ZzPrivateMsg)MsgVOToModel(message);
        if(messageSecretValidVo.getSendStatus().equals("1")){//如果可以发送消息
            saveMsg(privateMsg);
            //存储消息信息（新）
            String msgId = super.saveMessageInfo("USER",ip,msg);
            msgSendStatusVo.setId(msgId);
            //todo 发消息后期改成前端连接信息中心
            SocketMsgVo msgVo = new SocketMsgVo();
            /**消息确认id*/
            msgVo.setId(msgId);
            msgVo.setCode(jsonObject.getString("code"));
            msgVo.setSender(privateMsg.getMsgSender());
            msgVo.setReceiver(privateMsg.getMsgReceiver());
            msgVo.setMsg(msg);
            /**需要接收确认*/
            msgVo.setConfirmFlg(true);
            rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
           /* //如果不在线则不发
            String online = common.nulToEmptyString(RedisUtil.getValue(CacheConst.userOnlineCahce+privateMsg.getMsgReceiver()));

                if ((MessageType.ONLINE+"").equals(online)) {
                    SocketMsgVo msgVo = new SocketMsgVo();
                    msgVo.setCode(jsonObject.getString("code"));
                    msgVo.setSender(privateMsg.getMsgSender());
                    msgVo.setReceiver(privateMsg.getMsgReceiver());
                    msgVo.setMsg(msg);
                    //应答消息
                    MsgAnswerVO answerVO = super.msgAnswer(msg,privateMsg.getMsgId());
                    msgVo.setMsgAnswerVO(answerVO);
                    rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
                }else {
                    //告知对方不在线
                    *//*SocketMsgVo socketMsgVo = new SocketMsgVo();
                    socketMsgVo.setCode(MSG_ANSWER+"");
                    socketMsgVo.setSender((String)getJsonStringKeyValue(msg,"data.fromId"));
                    socketMsgVo.setReceiver((String)getJsonStringKeyValue(msg,"data.fromId"));
                    MsgAnswerVO answerVO = super.msgAnswer(msg,privateMsg.getMsgId(), MessageType.OFFLINE_ANSWER,"消息不能发送,对方不在线");
                    socketMsgVo.setMsgAnswerVO(answerVO);
                    rabbitMqMsgProducer.sendSocketMsgAnswer(socketMsgVo);*//*
                }
*/
        }else{//涉密词汇校验不通过
            msgSendStatusVo.setStatus(false);
            msgSendStatusVo.setContent("消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
            //todo 发消息后期改成前端连接信息中心
            SocketMsgVo socketMsgVo = new SocketMsgVo();
            socketMsgVo.setCode(MSG_ANSWER+"");
            socketMsgVo.setSender((String)getJsonStringKeyValue(msg,"data.fromId"));
            socketMsgVo.setReceiver((String)getJsonStringKeyValue(msg,"data.fromId"));
            MsgAnswerVO answerVO = super.msgAnswer(msg,privateMsg.getMsgId(), MessageType.FAIL_ANSWER,"消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
            socketMsgVo.setMsg(answerVO);
            rabbitMqMsgProducer.sendSocketMsgAnswer(socketMsgVo);
        }
        return msgSendStatusVo;
    }

    public void saveMsg(ZzPrivateMsg privateMsg){
        //privateMsgService.insert(privateMsg);
        super.saveNoReadMsg(privateMsg.getMsgSender(),privateMsg.getMsgReceiver());
    }
}
