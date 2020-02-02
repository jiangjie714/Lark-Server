package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.ZzDictionaryWords;
import com.workhub.z.servicechat.entity.ZzGroupMsg;
import com.workhub.z.servicechat.entity.ZzUserGroup;
import com.workhub.z.servicechat.service.ZzDictionaryWordsService;
import com.workhub.z.servicechat.service.ZzGroupMsgService;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;

import java.util.List;

import static com.workhub.z.servicechat.config.VoToEntity.*;

@Service
public class ProcessGroupMsg extends AbstractMsgProcessor {

    @Autowired
    protected ZzGroupMsgService groupMsgService;
    @Autowired
    protected ZzGroupService groupService;
    @Autowired
    protected ZzUserGroupService userGroupService;

    @Autowired
    protected ZzDictionaryWordsService zzDictionaryWordsService;

    public boolean sendMsg(ChannelContext channelContext, String msg) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
//      String code = jsonObject.getString("code");
        String message = jsonObject.getString("data");
        ZzGroupMsg zzGroupMsg = (ZzGroupMsg)GroupMsgVOToModel(message);
        //判断涉密词汇begin
        MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
        messageSecretValidVo.setSendStatus("1");//可以发送
        String level = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.secretLevel"));
        String type = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.type"));
        if(type.equals("1")){//如果是文字信息
            //文字内容
            String title = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title"));
            messageSecretValidVo = super.messageSecretValid(title,level);
        }
        //判断涉密词汇end

        if(userGroupService.queryInGroup(zzGroupMsg.getMsgSender(),zzGroupMsg.getMsgReceiver())>0) {
            if(messageSecretValidVo.getSendStatus().equals("1")){//如果可以发送消息
                Tio.sendToGroup(channelContext.getGroupContext(),zzGroupMsg.getMsgReceiver(),this.getWsResponse(msg));
                saveMsg(zzGroupMsg);
                //存储消息信息（新）
                super.saveMessageInfo("GROUP",zzGroupMsg.getMsgSender(),zzGroupMsg.getMsgReceiver()
                        ,zzGroupMsg.getLevels(),zzGroupMsg.getSendTime(),message,zzGroupMsg.getMsgId(),channelContext.getClientNode().getIp(),channelContext.getBsId());
                super.msgAnswer(msg,zzGroupMsg.getMsgId(),channelContext);
            }else{//
                super.msgAnswer(msg,zzGroupMsg.getMsgId(),channelContext, MessageType.FAIL_ANSWER,"消息不能发送，包含如下涉密词汇："+messageSecretValidVo.getSecretWords());
            }

        }
        return true;
    }

    public void saveMsg(ZzGroupMsg zzGroupMsg){
        groupMsgService.insert(zzGroupMsg);
        List<String> userList = groupService.queryGroupUserIdListByGroupId(zzGroupMsg.getMsgReceiver());
        if(userList == null|| userList.isEmpty()) {
            return;
        }
        for (int i = 0; i < userList.size() ; i++) {
            super.saveNoReadMsg(zzGroupMsg.getMsgReceiver(),userList.get(i));
        }
    }
}
