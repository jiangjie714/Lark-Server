package com.workhub.z.servicechat.processor;

import  com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.config.FileTypeEnum;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.group.ZzGroupFile;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.ZzGroupFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;

import java.util.Date;

import static com.workhub.z.servicechat.config.MessageType.*;

@Service
public class ProcessMsg extends AbstractMsgProcessor{

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

    public Object process(ChannelContext channelContext, String msg) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("data");

            //文件上传信息更新begin
            try {
                String msgType = common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.type"));
                //如果是文件或者图片上传
                if("2".equals(msgType)||"3".equals(msgType)){
                    ZzGroupFile zzGroupFile = new ZzGroupFile();
                    zzGroupFile.setId(RandomId.getUUID());
                    zzGroupFile.setFileId(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.id")));
                    zzGroupFile.setCreator(common.nulToEmptyString(common.getJsonStringKeyValue(message,"fromId")));
                    zzGroupFile.setCreatorName(common.nulToEmptyString(common.getJsonStringKeyValue(message,"username")));
                    zzGroupFile.setCreateTime(new Date());
                    zzGroupFile.setSizes(Double.parseDouble(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.fileSize"))));
                    zzGroupFile.setFileName(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title")));
                    zzGroupFile.setFileExt(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.extension")));
                    zzGroupFile.setFileType(FileTypeEnum.getEnumByValue(common.nulToEmptyString(zzGroupFile.getFileExt())).getType());
                    zzGroupFile.setLevels(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.secretLevel")));
                    zzGroupFile.setGroupId(common.nulToEmptyString(common.getJsonStringKeyValue(message,"toId")));
                    zzGroupFile.setReceiverName(common.nulToEmptyString(common.getJsonStringKeyValue(message,"toName")));
                    zzGroupFile.setApproveFlg("0");//默认都是审批不通过
                    boolean isGroup = (Boolean) common.getJsonStringKeyValue(message,"isGroup");
                    zzGroupFile.setIsGroup(isGroup?String.valueOf(MessageType.GROUP_FILE):String.valueOf(MessageType.PRIVATE_FILE));
                    if(zzGroupFile.getIsGroup().equals(String.valueOf(MessageType.GROUP_FILE))){
                        if(zzGroupFile.getLevels().equals("30")){//如果是非密文件
                            zzGroupFile.setApproveFlg("1");//直接审批通过
                        }
                    }
                    zzGroupFileService.fileRecord(zzGroupFile);
                    //记录群状态变动
                    ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
                    zzGroupStatus.setId(RandomId.getUUID());
                    zzGroupStatus.setOperatorName(common.nulToEmptyString(common.getJsonStringKeyValue(message,"username")));
                    zzGroupStatus.setOperator(common.nulToEmptyString(common.getJsonStringKeyValue(message,"fromId")));
                    zzGroupStatus.setOperateType(MessageType.FLOW_UPLOADFILE);//上传附件
                    zzGroupStatus.setGroupId(common.nulToEmptyString(common.getJsonStringKeyValue(message,"toId")));
                    zzGroupStatus.setOperateTime(new Date());
                    zzGroupStatus.setDescribe(common.nulToEmptyString(common.getJsonStringKeyValue(message,"username"))+
                                    "上传了附件："+
                                    common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title"))+
                            (((common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.extension"))).equals(""))?"":("."+common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.extension"))))
                            );
                    //zzGroupStatusService.add(zzGroupStatus);
                    rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                }
            } catch (Exception e) {
                //异常记录到日志
                log.error(common.getExceptionMessage(e));
            }
            //文件上传信息更新end

            switch (Integer.parseInt(code)){
                case SYS_MSG:
//                  Tio.sendToAll(channelContext.getGroupContext(),wsResponse);
                    break;
                case GROUP_MSG:
                   return processGroupMsg.sendMsg(channelContext,msg);
                case MEET_MSG:
                   return processMeetMsg.sendMsg(channelContext,msg);
                case MEETING_ADD:
                    return processMeetMsg.meetUserChange(channelContext,msg);
                case PRIVATE_MSG:
                    return  processPrivateMsg.sendMsg(channelContext,msg);
                case GROUP_EDIT:
                     processEditGroup.processManage(channelContext,message);
                    break;
                case GROUP_CREATE:
                    return processEditGroup.createGroup(channelContext,msg);
                case MSG_EDIT_READ:
                    JSONObject temp = JSONObject.parseObject(message);
                        super.deleteNoReadMsg(temp.getString("sender"),temp.getString("reviser"),temp.getString("reviserName"),temp.getString("senderName"));
                    break;
                default:
                    System.out.println("你说的什么鬼");
                    break;
            }
        }catch (Exception e){
            System.out.println("别提了又错了"+ e.getMessage());
            return null;
        }
        return null;
    }
}
