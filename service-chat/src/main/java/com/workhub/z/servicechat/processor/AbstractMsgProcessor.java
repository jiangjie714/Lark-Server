package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.entity.ZzContactInf;
import com.workhub.z.servicechat.entity.config.ZzDictionaryWords;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.entity.group.ZzGroupFile;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.entity.message.ZzMegReadLog;
import com.workhub.z.servicechat.entity.message.ZzMessageInfo;
import com.workhub.z.servicechat.entity.message.ZzMsgReadRelation;
import com.workhub.z.servicechat.model.MeetingDto;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;

import java.util.Date;
import java.util.List;

import static com.workhub.z.servicechat.config.Common.getJsonStringKeyValue;
import static com.workhub.z.servicechat.config.Common.stringSearch;
import static com.workhub.z.servicechat.config.MessageType.SUCCESS_ANSWER;
import static com.workhub.z.servicechat.config.RandomId.getUUID;

@Service
public class AbstractMsgProcessor {
    private Logger logger = LoggerFactory.getLogger(AbstractMsgProcessor.class);
    @Autowired
    ZzDictionaryWordsService dictionaryWordsService;
    @Autowired
    ZzMsgReadRelationService msgReadRelationService;
    @Autowired
    ZzMessageInfoService messageInfoService;
    @Autowired
    AdminUserService iUserService;
    @Autowired
    ZzGroupService zzGroupService;
    @Autowired
    ZzMegReadLogService megReadLogService;
    @Autowired
    ZzMeetingService zzMeetingService;
    @Autowired
    ZzGroupFileService zzGroupFileService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    @Autowired
    ZzContactService zzContactService;
    // TODO: 2019/5/31 保密，关键词过滤

    public String messageFiltering(String msg){
        // TODO: 2019/5/31  获取数据词典，涉密词汇
        List<ZzDictionaryWords> zzDictionaryWordsList =null;
        return stringSearch(msg,zzDictionaryWordsList);
    }
    //消息密级校验结果
    //msgtxt 消息内容；msglevels 消息密级
    public MessageSecretValidVo messageSecretValid(String msgtxt, String msglevels){
        List<ZzDictionaryWords> zzDictionaryWordsList = null;
        if("30".equals(msglevels)){
            zzDictionaryWordsList = this.dictionaryWordsService.getSecretWordList40();
            zzDictionaryWordsList.addAll(this.dictionaryWordsService.getSecretWordList60());//合并两个列表40/60
        }else if("40".equals(msglevels)){
            zzDictionaryWordsList = this.dictionaryWordsService.getSecretWordList60();
        }else if("60".equals(msglevels)){
            MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
            messageSecretValidVo.setSendStatus("1");
            try {
                Common.putVoNullStringToEmptyString(messageSecretValidVo);
            }catch (Exception e){
                logger.error("过滤敏感词汇出错！");
                logger.error(Common.getExceptionMessage(e));
            }
            return messageSecretValidVo;
        }
        if(msgtxt==null){
            msgtxt="";
        }
        return Common.checkMessageSecretQuick(msgtxt,zzDictionaryWordsList);
    }
    // TODO: 2019/5/31 敏感词过滤
    
    public boolean checkUserOnline(ChannelContext channelContext, String userId){
        ChannelContext checkChannelContext =
                Tio.getChannelContextById(channelContext.getGroupContext(),userId);
        //检查是否在线
        boolean isOnline = checkChannelContext != null && !checkChannelContext.isClosed;
        return isOnline;
    }

    /**
    *@Description: 存储未读消息
    *@Param:
    *@return:
    *@Author: 忠
    *@date: 2019/6/12
    */
    public void saveNoReadMsg(String sender, String receiver){
        ZzMsgReadRelation msgReadRelation = new ZzMsgReadRelation();
        msgReadRelation.setId(getUUID());
        msgReadRelation.setReceiver(receiver);
        msgReadRelation.setSender(sender);
        msgReadRelation.setSendType("1");
        msgReadRelationService.insert(msgReadRelation);
    }

    /**
    *@Description: 清除未读消息
    *@Param:
    *@return:
    *@Author: 忠
    *@date: 2019/6/12
    */
    public void deleteNoReadMsg(String sender, String receiver, String receiverName, String senderName){
        ZzMegReadLog megReadLog = new ZzMegReadLog();
        megReadLog.setId(getUUID());
        megReadLog.setReadtime(new Date());
        megReadLog.setReviser(receiver);
        megReadLog.setSender(sender);
        megReadLog.setReviserName(receiverName);
        megReadLog.setSenderName(senderName);
        Common.nulToEmptyString(megReadLog);
        megReadLogService.insert(megReadLog);
        msgReadRelationService.deleteByConsumerAndSender(sender,receiver);
        //通知发消息的人，接收人已经点开了消息的页面
        //判断发消息和接收人是否是私人
        ZzContactInf senderContact = zzContactService.queryById(sender);
        ZzContactInf receiverContact = zzContactService.queryById(receiver);
        if(senderContact!=null &&
                receiverContact !=null &&
                senderContact.getType().equals("USER") &&
                receiverContact.getType().equals("USER")){
            //todo 改成socket代码规范
            SocketMsgVo msgVo = new SocketMsgVo();
            msgVo.setCode(SocketMsgTypeEnum.SINGLE_MSG);
            msgVo.setSender(receiver);
            msgVo.setReceiver(sender);
            SocketMsgReaderVo readerVo = new SocketMsgReaderVo();
            readerVo.setReaderId(receiver);
            readerVo.setSenderId(sender);
            SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
            detailVo.setData(readerVo);
            detailVo.setCode(SocketMsgDetailTypeEnum.PRIVATE_RECEIVER_OPEN_BOARD);
            msgVo.setMsg(detailVo);
            //todo SocketMsgVo加密
            rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
        }

    }

    /**
    *@Description: 存储消息
    *@Param: sender，receiver，createtime，content，levels,megid
    *@return:
    *@Author: 忠
    *@date: 2019/6/23
    *//*
    public void saveMessageInfo(String type,String sender, String receiver, String levels, Date createtime, String content, String msgId,String ip,String senderSN){
        ZzMessageInfo messageInfo = new ZzMessageInfo();
        messageInfo.setContent(content);
        messageInfo.setCreatetime(createtime);
        messageInfo.setLevels(levels);
        messageInfo.setReceiver(receiver);
        messageInfo.setSender(sender);
        messageInfo.setMsgId(msgId);
        messageInfo.setType(type);
        messageInfo.setIp(ip);
        messageInfo.setSenderSN(senderSN);

        //0科室内1跨科室2跨场所
        String iscross = MessageType.CROSSTYPE_SAME_OFFICE;
        if("GROUP".equals(type)){//如是群消息
            ZzGroup group = zzGroupService.queryById(receiver);
            iscross = group.getIscross();
        }else if("MEET".equals(type)){//如果是会议
            MeetingDto group = zzMeetingService.getMeetInf(receiver);
            iscross = group.getIscross();
        }else{//如果是私聊消息
            List<UserInfo> userInfoList = iUserServiceUserService.userList(sender+","+receiver);
            UserInfo senderInf = userInfoList.get(0)==null?new UserInfo():userInfoList.get(0);
            UserInfo receiveInf = userInfoList.get(1)==null?new UserInfo():userInfoList.get(1);
            String senderOrg = Common.nulToEmptyString(senderInf.getOrgCode());
            String receiverOrg = Common.nulToEmptyString(receiveInf.getOrgCode());
            //以下逻辑照搬admin服务OrgBiz的getOrgUsers方法
            if(senderOrg.equals("") || receiverOrg.equals("")){
                //跨场所
                iscross = MessageType.CROSSTYPE_DIFF_WORKSPACE;
            }else{
                if(senderOrg.equals(receiverOrg)){
                    //科室内
                    iscross = MessageType.CROSSTYPE_SAME_OFFICE;
                }else {
                    String parentOrgCode;
                    //过滤外网错误数据，内网正常人员不会出现5为以下的orgCode
                    if (senderOrg.length() > 6) {
                        //为院机关
                        if (!MessageType.ORG_CODE_INSTITUTE_ORGAN.equals(parentOrgCode = senderOrg.substring(0, 6))) {
                            parentOrgCode = senderOrg.substring(0, 4);
                        }
                    } else {
                        parentOrgCode = senderOrg;
                    }
                    final String finalParentOrgCode = parentOrgCode;
                    if(receiverOrg.length()<6){
                        //跨场所
                        iscross = MessageType.CROSSTYPE_DIFF_WORKSPACE;
                    }else {
                        if(receiverOrg.substring(0, 6).contains(finalParentOrgCode)){
                            //跨科室
                            iscross = MessageType.CROSSTYPE_DIFF_OFFICE;
                        }else{
                            //跨场所
                            iscross = MessageType.CROSSTYPE_DIFF_WORKSPACE;
                        }
                    }

                }
            }
        }
        messageInfo.setIscross(iscross);
        messageInfoService.insert(messageInfo);
    }
*/
    /**
     * 保存消息内容
     * @param type USER/GROUP/MEET 消息类型
     * @param ip 客户端ip
     * @param msgJosnInf 消息json体
     */
    public String saveMessageInfo(String type,String ip,String msgJosnInf) throws Exception{
        String msgId = getUUID();//信息ID
        ZzMessageInfo messageInfo = new ZzMessageInfo();
        ZzContactInf zzContactInfSender = new ZzContactInf();
        ZzContactInf zzContactInfReceiver = new ZzContactInf();
        JSONObject jsonObject = JSONObject.parseObject(msgJosnInf);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("data");
        //文件上传信息更新begin
        try {
            String msgType = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.type"));
            //如果是文件或者图片上传
            if("2".equals(msgType)||"3".equals(msgType)){
                ZzGroupFile zzGroupFile = new ZzGroupFile();
                zzGroupFile.setId(RandomId.getUUID());
                zzGroupFile.setFileId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.id")));
                zzGroupFile.setCreator(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"fromId")));
                zzGroupFile.setCreatorName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"username")));
                zzGroupFile.setCreateTime(new Date());
                zzGroupFile.setSizes(Double.parseDouble(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.fileSize"))));
                zzGroupFile.setFileName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title")));
                zzGroupFile.setFileExt(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.extension")));
                zzGroupFile.setFileType(FileTypeEnum.getEnumByValue(Common.nulToEmptyString(zzGroupFile.getFileExt())).getType());
                zzGroupFile.setLevels(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.secretLevel")));
                zzGroupFile.setGroupId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"toId")));
                zzGroupFile.setReceiverName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"toName")));
                zzGroupFile.setApproveFlg("0");//默认都是审批不通过
                boolean isGroup = (Boolean) Common.getJsonStringKeyValue(message,"isGroup");
                zzGroupFile.setIsGroup(isGroup?String.valueOf(MessageType.GROUP_FILE):String.valueOf(MessageType.PRIVATE_FILE));
                if(zzGroupFile.getIsGroup().equals(String.valueOf(MessageType.GROUP_FILE))){
                    if(zzGroupFile.getLevels().equals(MessageType.NO_SECRECT_LEVEL)){//如果是非密文件
                        zzGroupFile.setApproveFlg("1");//直接审批通过
                    }
                }
                zzGroupFileService.fileRecord(zzGroupFile);
                //记录群状态变动
                ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
                zzGroupStatus.setId(RandomId.getUUID());
                zzGroupStatus.setOperatorName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"username")));
                zzGroupStatus.setOperator(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"fromId")));
                zzGroupStatus.setOperateType(MessageType.FLOW_UPLOADFILE);//上传附件
                zzGroupStatus.setGroupId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"toId")));
                zzGroupStatus.setOperateTime(new Date());
                zzGroupStatus.setDescribe(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"username"))+
                        "上传了附件："+
                        Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title"))+
                        (((Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.extension"))).equals(""))?"":("."+ Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.extension"))))
                );
                try{
                    rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                }catch (Exception ex){
                    logger.error("聊天附件mq发送消息异常");
                    logger.error(Common.getExceptionMessage(ex));
                }

            }

        //文件上传信息更新end
        //保存消息begin
        String receiver  = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"toId"));
        String sender = Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"fromId"));
        messageInfo.setMsgId(msgId);
        messageInfo.setCreatetime(new Date());
        messageInfo.setLevels(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.secretLevel")));
        messageInfo.setReceiver(receiver);
        messageInfo.setSender(sender);
        messageInfo.setType(type);
        messageInfo.setIp(ip);
        messageInfo.setFrontId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"id")));
        messageInfo.setContent(message);
        messageInfo.setFileType(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.type")));

        /*JSONArray atUsers = ((JSONArray) Common.getJsonStringKeyValue(message,"atId"));
        if(atUsers.size()!=0){
            String atNames = "";
            for(int i=0;i<atUsers.size();i++){
                JSONObject atUser = JSON.parseObject(atUsers.getString(i));
                atNames += " "+atUser.getString("name");
            }
            if(!"".equals(atNames)){
                atNames = atNames.substring(1);
            }
        }*/
        messageInfo.setMsg(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title")));
        messageInfo.setFileId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.id")));
        //0科室内1跨科室2跨场所
        String iscross = MessageType.CROSSTYPE_SAME_OFFICE;
        if("GROUP".equals(type)){//如是群消息
            ZzGroup group = zzGroupService.queryById(receiver);
            iscross = group.getIscross();
        }else if("MEET".equals(type)){//如果是会议
            MeetingDto group = zzMeetingService.getMeetInf(receiver);
            iscross = group.getIscross();
        }else{//如果是私聊消息
            List<ChatAdminUserVo> userInfoList = iUserService.userList(sender+","+receiver);
            ChatAdminUserVo senderInf = userInfoList.get(0)==null?new ChatAdminUserVo():userInfoList.get(0);
            ChatAdminUserVo receiveInf = userInfoList.get(1)==null?new ChatAdminUserVo():userInfoList.get(1);
            String senderOrg = Common.nulToEmptyString(senderInf.getOrgCode());
            String receiverOrg = Common.nulToEmptyString(receiveInf.getOrgCode());
            //以下逻辑照搬admin服务OrgBiz的getOrgUsers方法
            if(senderOrg.equals("") || receiverOrg.equals("")){
                //跨场所
                iscross = MessageType.CROSSTYPE_DIFF_WORKSPACE;
            }else{
                if(senderOrg.equals(receiverOrg)){
                    //科室内
                    iscross = MessageType.CROSSTYPE_SAME_OFFICE;
                }else {
                    String parentOrgCode;
                    //过滤外网错误数据，内网正常人员不会出现5为以下的orgCode
                    if (senderOrg.length() > 6) {
                        //为院机关
                        if (!MessageType.ORG_CODE_INSTITUTE_ORGAN.equals(parentOrgCode = senderOrg.substring(0, 6))) {
                            parentOrgCode = senderOrg.substring(0, 4);
                        }
                    } else {
                        parentOrgCode = senderOrg;
                    }
                    final String finalParentOrgCode = parentOrgCode;
                    if(receiverOrg.length()<6){
                        //跨场所
                        iscross = MessageType.CROSSTYPE_DIFF_WORKSPACE;
                    }else {
                        if(receiverOrg.substring(0, 6).contains(finalParentOrgCode)){
                            //跨科室
                            iscross = MessageType.CROSSTYPE_DIFF_OFFICE;
                        }else{
                            //跨场所
                            iscross = MessageType.CROSSTYPE_DIFF_WORKSPACE;
                        }
                    }

                }
            }
        }
        messageInfo.setIscross(iscross);
        messageInfoService.insert(messageInfo);
        //保存消息end
        //保存联系人信息begin
        String senderCnt = this.zzContactService.countsById(sender);
        //如果没有该联系人信息 发送人一个是单个人
        if("0".equals(senderCnt)){
            zzContactInfSender.setId(sender);
            zzContactInfSender.setType("USER");
            zzContactInfSender.setAvartar(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"avatar")));
            zzContactInfSender.setName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"username")));
            zzContactInfSender.setMemberNum("2");
            zzContactInfSender.setGroupOwner("");
            ChatAdminUserVo userSenderInf = this.iUserService.getUserInfo(sender);
            if(userSenderInf == null){
                userSenderInf = new ChatAdminUserVo();
                Common.putVoNullStringToEmptyString(userSenderInf);
            }
            zzContactInfSender.setLevels(userSenderInf.getSecretLevel());
            zzContactInfSender.setPid(userSenderInf.getPId());
            Common.putVoNullStringToEmptyString(zzContactInfSender);
            this.zzContactService.add(zzContactInfSender);
        }
        String receiverCnt = this.zzContactService.countsById(receiver);
        //如果没有该联系人信息 接收人是单人，也有可能是群或者会议
        if("0".equals(receiverCnt)){
            zzContactInfReceiver.setId(receiver);
            zzContactInfReceiver.setType(type);
            zzContactInfReceiver.setName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"toName")));
            if("GROUP".equals(type) || "MEET".equals(type)){
                zzContactInfReceiver.setAvartar(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"contactInfo.avatar")));
                zzContactInfReceiver.setLevels(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"contactInfo.secretLevel")));
                zzContactInfReceiver.setPid("");
                zzContactInfReceiver.setMemberNum(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"contactInfo.memberNum")));
                zzContactInfReceiver.setGroupOwner(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"contactInfo.groupOwnerId")));
            }else if("USER".equals(type)){
                ChatAdminUserVo userReceiverInf = this.iUserService.getUserInfo(receiver);
                if(userReceiverInf == null){
                    userReceiverInf = new ChatAdminUserVo();
                    Common.putVoNullStringToEmptyString(userReceiverInf);
                }
                zzContactInfReceiver.setLevels(userReceiverInf.getSecretLevel());
                zzContactInfReceiver.setPid(userReceiverInf.getPId());
                zzContactInfReceiver.setAvartar(userReceiverInf.getAvatar());
                zzContactInfSender.setGroupOwner("");
                zzContactInfReceiver.setMemberNum("2");
            }
            Common.putVoNullStringToEmptyString(zzContactInfReceiver);
            this.zzContactService.add(zzContactInfReceiver);
        }
        //保存联系人信息end
        } catch (Exception e) {
            //异常记录到日志
            logger.error(Common.getExceptionMessage(e));
            throw e;
        }
        return msgId;
    }
    /**
    *@Description: 应答信息
    *@Param:消息内容
    *@return:
    *@Author: 忠
    *@date: 2019/7/30
    */
    // TODO: 2019/9/16 应答信息
    public SocketMsgDetailVo msgAnswer(String msg, String nId) throws Exception {
        return msgAnswer(msg,nId,SUCCESS_ANSWER,"发送成功");
    }
    //应答消息重载加入 状态 和 应答内容
    public SocketMsgDetailVo msgAnswer(String msg, String nId, int status, String content) throws Exception {
        SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
        detailVo.setCode(SocketMsgDetailTypeEnum.PRIVATE_SEND_ANSWER);
        MsgAnswerVo msgAnswerVO = new MsgAnswerVo();
        msgAnswerVO.setContactId((String)getJsonStringKeyValue(msg,"data.toId"));
        msgAnswerVO.setnId(nId);
        msgAnswerVO.setStatus(status);
        msgAnswerVO.setContent(content);
        detailVo.setData(msgAnswerVO);
        return  detailVo;
   }

}
