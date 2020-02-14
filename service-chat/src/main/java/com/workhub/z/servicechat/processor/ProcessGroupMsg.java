package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.VO.MsgAnswerVO;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.ZzGroupMsg;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.ZzDictionaryWordsService;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.workhub.z.servicechat.config.MessageType.MSG_ANSWER;
import static com.workhub.z.servicechat.config.VoToEntity.GroupMsgVOToModel;
import static com.workhub.z.servicechat.config.common.getJsonStringKeyValue;

@Service
public class ProcessGroupMsg extends AbstractMsgProcessor {

    @Autowired
    protected ZzGroupService groupService;
    @Autowired
    protected ZzUserGroupService userGroupService;

    @Autowired
    protected ZzDictionaryWordsService zzDictionaryWordsService;
    @Autowired
    private RabbitMqMsgProducer rabbitMqMsgProducer;

    public MsgSendStatusVo sendMsg(String msg, String ip) throws Exception {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();
        try {

            JSONObject jsonObject = JSONObject.parseObject(msg);
    //      String code = jsonObject.getString("code");
            String message = jsonObject.getString("data");
            ZzGroupMsg zzGroupMsg = (ZzGroupMsg)GroupMsgVOToModel(message);
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

            if(userGroupService.queryInGroup(zzGroupMsg.getMsgSender(),zzGroupMsg.getMsgReceiver())>0) {
                if(messageSecretValidVo.getSendStatus().equals("1")){//如果可以发送消息
                        saveMsg(zzGroupMsg);
                        //存储消息信息（新）
                        String msgId = super.saveMessageInfo("GROUP",ip,msg);
                        msgSendStatusVo.setId(msgId);

                        SocketMsgVo msgVo = new SocketMsgVo();
                        msgVo.setCode(jsonObject.getString("code"));
                        msgVo.setSender(zzGroupMsg.getMsgSender());
                        msgVo.setReceiver(zzGroupMsg.getMsgReceiver());
                        msgVo.setMsg(msg);
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

            }

        }catch (Exception e){
            throw e;
        }
        return msgSendStatusVo;
    }

    public void saveMsg(ZzGroupMsg zzGroupMsg){
        //groupMsgService.insert(zzGroupMsg);
        List<String> userList = groupService.queryGroupUserIdListByGroupId(zzGroupMsg.getMsgReceiver());
        if(userList == null|| userList.isEmpty()) {
            return;
        }
        for (int i = 0; i < userList.size() ; i++) {
            super.saveNoReadMsg(zzGroupMsg.getMsgReceiver(),userList.get(i));
        }
    }
}
