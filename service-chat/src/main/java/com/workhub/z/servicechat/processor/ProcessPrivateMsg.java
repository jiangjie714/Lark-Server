package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.VO.SocketMsgDetailVo;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.entity.message.ZzPrivateMsg;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import com.workhub.z.servicechat.service.impl.ZzUserOnlineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.workhub.z.servicechat.config.Common.getJsonStringKeyValue;
import static com.workhub.z.servicechat.config.VoToEntity.MsgVOToModel;

@Service
public class ProcessPrivateMsg extends AbstractMsgProcessor {

    @Autowired
    protected ZzPrivateMsgService privateMsgService;
    @Autowired
    ZzUserOnlineServiceImpl zzUserOnlineService;

    public MsgSendStatusVo sendMsg(String msg, String ip) throws Exception {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);

//      String code = jsonObject.getString("code");
            String message = jsonObject.getString("data");
            //判断涉密词汇begin
            MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
            messageSecretValidVo.setSendStatus("1");//可以发送
            String level = Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "content.secretLevel"));
            String type = Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "content.type"));

            String oId = Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "id"));
            msgSendStatusVo.setOId(oId);
            if (type.equals("1")) {//如果是文字信息
                //文字内容
                String title = Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "content.title"));
                messageSecretValidVo = super.messageSecretValid(title, level);
            }
            //判断涉密词汇end
            ZzPrivateMsg privateMsg = (ZzPrivateMsg) MsgVOToModel(message);
            if (messageSecretValidVo.getSendStatus().equals("1")) {//如果可以发送消息
                //校验长度
                if(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title")).length()>300){
                    msgSendStatusVo.setStatus(false);
                    msgSendStatusVo.setContent("消息不能发送，超过最大字数限制300");
                    return msgSendStatusVo;
                }
                String msgId = "";
                String msgReceiver = privateMsg.getMsgReceiver();

                List<String> onlineServerList = OnLineServerConfig.getOnlineServerList();
                //接收人是否在线客服消息
                boolean isOnlineServerMsg = false;
                //发送人在线客服
                boolean isOnlineServerSender = false;

                String toId = Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "toId"));
                String fromId = Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "fromId"));

                if(OnLineServerConfig.getOnlineServerId().equals(toId)){//如果是发给在线客服
                    isOnlineServerMsg = true;//在线客服消息
                }
                if(onlineServerList.contains(fromId)){
                    isOnlineServerSender = true;//客服解答消息
                }

                if(isOnlineServerMsg&&isOnlineServerSender){
                    msgSendStatusVo.setStatus(false);
                    msgSendStatusVo.setContent("在线客服之间不能发送消息");
                    return msgSendStatusVo;
                }

                if(isOnlineServerMsg){//如果是发给在线客服，不存消息
                    //在线客服消息id都是999999
                    msgId = MessageType.ONLINE_MSG_ID;

                    List<String> chooseServerList = new ArrayList<>();//可选的在线客服
                    for(String serverId:onlineServerList){
                        if(zzUserOnlineService.isUserOnline(serverId)){
                            chooseServerList.add(serverId);
                        }
                    }
                    if(chooseServerList.size()==0){
                        msgSendStatusVo.setStatus(false);
                        msgSendStatusVo.setContent("在线客服不在线，无法发送");
                        return msgSendStatusVo;
                    }else {
                        int randInt = Common.getIntRandom(0,chooseServerList.size()-1);
                        msgReceiver = chooseServerList.get(randInt);//随机选择一个在线的客服
                    }
                }else if(isOnlineServerSender){//如果在线客服发送消息
                    //在线客服消息id都是999999
                    msgId = MessageType.ONLINE_MSG_ID;
                }else{//聊天消息
                    saveMsg(privateMsg);
                    //存储消息信息（新）
                    msgId = super.saveMessageInfo("USER", ip, msg);
                }
                msgSendStatusVo.setId(msgId);
                //把前端的消息id替换成后端的id
                String newMsg = Common.setJsonStringKeyValue(msg,"data.id",msgId);

                if(isOnlineServerSender){//如果发送人是客服，替换发送人id 999999
                    newMsg = Common.setJsonStringKeyValue(newMsg,"data.fromId",OnLineServerConfig.getOnlineServerId());
                }
                SocketMsgVo msgVo = new SocketMsgVo();
                /**消息确认id*/
                msgVo.setId(msgId);
                msgVo.setCode(SocketMsgTypeEnum.SINGLE_MSG);
                msgVo.setSender(privateMsg.getMsgSender());
                msgVo.setReceiver(msgReceiver);
                SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
                for(SocketMsgDetailTypeEnum senum:SocketMsgDetailTypeEnum.values()){
                    if(senum.getCode().equals(jsonObject.getString("code"))){
                        detailVo.setCode(senum);
                        break;
                    }

                }
                detailVo.setData(Common.getJsonStringKeyValue(newMsg,"data"));
                msgVo.setMsg(detailVo);
                /**需要接收确认*/
                if(!isOnlineServerMsg && !isOnlineServerSender){//如果不是客服消息，需要接收确认
                    msgVo.setConfirmFlg(true);
                }
                //todo SocketDetailMsgVo加密
                msgSendStatusVo.setMsg(msgVo);
                //todo 发消息后期改成前端连接信息中心
                //todo 测试使用
                //rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
           /* //如果不在线则不发
            String online = Common.nulToEmptyString(RedisUtil.getValue(CacheConst.userOnlineCahce+privateMsg.getMsgReceiver()));

                if ((MessageType.ONLINE+"").equals(online)) {
                    SocketMsgVo msgVo = new SocketMsgVo();
                    msgVo.setCode(jsonObject.getString("code"));
                    msgVo.setSender(privateMsg.getMsgSender());
                    msgVo.setReceiver(privateMsg.getMsgReceiver());
                    msgVo.setMsg(msg);
                    //应答消息
                    MsgAnswerVo answerVO = super.msgAnswer(msg,privateMsg.getMsgId());
                    msgVo.setMsgAnswerVO(answerVO);
                    rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
                }else {
                    //告知对方不在线
                    *//*SocketMsgVo socketMsgVo = new SocketMsgVo();
                    socketMsgVo.setCode(MSG_ANSWER+"");
                    socketMsgVo.setSender((String)getJsonStringKeyValue(msg,"data.fromId"));
                    socketMsgVo.setReceiver((String)getJsonStringKeyValue(msg,"data.fromId"));
                    MsgAnswerVo answerVO = super.msgAnswer(msg,privateMsg.getMsgId(), MessageType.OFFLINE_ANSWER,"消息不能发送,对方不在线");
                    socketMsgVo.setMsgAnswerVO(answerVO);
                    rabbitMqMsgProducer.sendSocketMsgAnswer(socketMsgVo);*//*
                }
*/
            } else {//涉密词汇校验不通过
                msgSendStatusVo.setStatus(false);
                msgSendStatusVo.setContent("消息不能发送，包含如下涉密词汇：" + messageSecretValidVo.getSecretWords());

                SocketMsgVo socketMsgVo = new SocketMsgVo();
                socketMsgVo.setCode(SocketMsgTypeEnum.SINGLE_MSG);
                socketMsgVo.setSender((String) getJsonStringKeyValue(msg, "data.fromId"));
                socketMsgVo.setReceiver((String) getJsonStringKeyValue(msg, "data.fromId"));
                SocketMsgDetailVo answerVO = super.msgAnswer(msg, privateMsg.getMsgId(), MessageType.FAIL_ANSWER, "消息不能发送，包含如下涉密词汇：" + messageSecretValidVo.getSecretWords());
                socketMsgVo.setMsg(answerVO);
                //todo 发消息后期改成前端连接信息中心
                //rabbitMqMsgProducer.sendSocketPrivateMsg(socketMsgVo);
            }
        }catch (Exception e){
            throw e;
        }
        return msgSendStatusVo;
    }

    public void saveMsg(ZzPrivateMsg privateMsg){
        //privateMsgService.insert(privateMsg);
        super.saveNoReadMsg(privateMsg.getMsgSender(),privateMsg.getMsgReceiver());
    }
}
