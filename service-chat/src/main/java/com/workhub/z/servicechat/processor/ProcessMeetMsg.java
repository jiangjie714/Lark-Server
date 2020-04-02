package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.group.ZzGroupMsg;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;

import static com.workhub.z.servicechat.config.VoToEntity.GroupMsgVOToModel;

@Service
public class ProcessMeetMsg extends AbstractMsgProcessor{

    public boolean sendMsg(ChannelContext channelContext, String msg) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String message = jsonObject.getString("data");
        ZzGroupMsg zzGroupMsg = (ZzGroupMsg)GroupMsgVOToModel(message);
        //判断涉密词汇begin
        MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
        //可以发送
        messageSecretValidVo.setSendStatus("1");
        String level = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.secretLevel"));
        String type = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.type"));
        if(type.equals("1")){
            //如果是文字信息
            //文字内容
            String title = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title"));
            messageSecretValidVo = super.messageSecretValid(title,level);
        }
        //判断涉密词汇end
        if(messageSecretValidVo.getSendStatus().equals("1")){
            super.saveMessageInfo("MEET",zzGroupMsg.getMsgSender(),zzGroupMsg.getMsgReceiver()
                    ,zzGroupMsg.getLevels(),zzGroupMsg.getSendTime(),message,zzGroupMsg.getMsgId(),channelContext.getClientNode().getIp(),channelContext.getBsId());
            //如果可以发送消息
            Tio.sendToGroup(channelContext.getGroupContext(),zzGroupMsg.getMsgReceiver(),this.getWsResponse(msg));
            //存储消息信息（新）
             super.msgAnswer(msg,zzGroupMsg.getMsgId(),channelContext);
        }else{//
            super.msgAnswer(msg,zzGroupMsg.getMsgId(),channelContext, MessageType.FAIL_ANSWER,"消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
        }
        return true;
    }

    public Boolean meetUserChange(ChannelContext channelContext, String msg) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String message = jsonObject.getString("data");
        String meetId =common.getJsonStringKeyValue(message,"id").toString();
        Tio.bindGroup(channelContext,meetId);
        return true;
    }
}
