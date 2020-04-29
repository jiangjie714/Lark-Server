package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.ZzGroupFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.workhub.z.servicechat.config.MessageType.*;

@Service
public class ProcessMsg extends AbstractMsgProcessor {

    @Autowired
    private ProcessPrivateMsg processPrivateMsg;
    @Autowired
    private ProcessEditGroup processEditGroup;
    @Autowired
    private ProcessGroupMsg processGroupMsg;
    @Autowired
    private ProcessMeetMsg processMeetMsg;
    @Autowired
    private ZzGroupFileService zzGroupFileService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    private static Logger log = LoggerFactory.getLogger(ProcessMsg.class);
    /**
     * 消息处理入口
     */

    public MsgSendStatusVo process(String userId, String messageInf, String ip) {
        MsgSendStatusVo sendStatusVo = null;
        try{
            JSONObject msgInf = JSONObject.parseObject(messageInf);
            String msg = msgInf.getString("messageInf");
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("data");
            switch (Integer.parseInt(code)){
                case SYS_MSG:
//                  Tio.sendToAll(channelContext.getGroupContext(),wsResponse);
                    break;
                case GROUP_MSG:
                    sendStatusVo = processGroupMsg.sendMsg(msg,ip);
                    break;
                case MEET_MSG:
                    sendStatusVo =  processMeetMsg.sendMsg(msg,ip);
                   break;
                case MEETING_ADD:
                    sendStatusVo =  processMeetMsg.meetUserChange(userId,msg);
                    break;
                case PRIVATE_MSG:
                    sendStatusVo =   processPrivateMsg.sendMsg(msg,ip);
                    break;
                case GROUP_EDIT:
                    sendStatusVo =   processEditGroup.processManage(userId,message);
                    break;
                case GROUP_CREATE:
                    sendStatusVo =    processEditGroup.createGroup(userId,msg);
                    break;
                case MSG_EDIT_READ:
                    JSONObject temp = JSONObject.parseObject(message);
                        super.deleteNoReadMsg(temp.getString("sender"),temp.getString("reviser"),temp.getString("reviserName"),temp.getString("senderName"));
                    break;
                default:
                    // TODO: 2020/2/22 消息类型未知错误
                    System.out.println("你说的什么鬼");
                    break;
            }
        }catch (Exception e){
            // TODO: 2020/2/22   消息解析错误 向前端返回错误信息
            System.out.println("别提了又错了"+ e.getMessage());
            if(sendStatusVo==null){
                sendStatusVo = new MsgSendStatusVo();
            }
            sendStatusVo.setStatus(false);
            sendStatusVo.setContent("服务器出错");
        }
        if(sendStatusVo==null){
            sendStatusVo = new MsgSendStatusVo();
        }
        return sendStatusVo;
    }
}
