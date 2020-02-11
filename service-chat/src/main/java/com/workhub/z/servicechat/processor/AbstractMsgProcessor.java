package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.VO.MsgAnswerVO;
import com.workhub.z.servicechat.config.FileTypeEnum;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.*;
import com.workhub.z.servicechat.feign.IUserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.workhub.z.servicechat.config.MessageType.MSG_ANSWER;
import static com.workhub.z.servicechat.config.MessageType.SUCCESS_ANSWER;
import static com.workhub.z.servicechat.config.RandomId.getUUID;
import static com.workhub.z.servicechat.config.common.getJsonStringKeyValue;
import static com.workhub.z.servicechat.config.common.stringSearch;
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
    IUserService iUserServiceUserService;
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
    public MessageSecretValidVo messageSecretValid(String msgtxt,String msglevels){
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
                common.putVoNullStringToEmptyString(messageSecretValidVo);
            }catch (Exception e){
                logger.error("过滤敏感词汇出错！");
                logger.error(common.getExceptionMessage(e));
            }
            return messageSecretValidVo;
        }
        if(msgtxt==null){
            msgtxt="";
        }
        return common.checkMessageSecretQuick(msgtxt,zzDictionaryWordsList);
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
        common.nulToEmptyString(megReadLog);
        megReadLogService.insert(megReadLog);
        msgReadRelationService.deleteByConsumerAndSender(sender,receiver);
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
            String senderOrg = common.nulToEmptyString(senderInf.getOrgCode());
            String receiverOrg = common.nulToEmptyString(receiveInf.getOrgCode());
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
    public String saveMessageInfo(String type,String ip,String msgJosnInf) {
        String msgId = getUUID();//信息ID
        ZzMessageInfo messageInfo = new ZzMessageInfo();
        ZzContactInf zzContactInfSender = new ZzContactInf();
        ZzContactInf zzContactInfReceiver = new ZzContactInf();
        JSONObject jsonObject = JSONObject.parseObject(msgJosnInf);
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
                    if(zzGroupFile.getLevels().equals(MessageType.NO_SECRECT_LEVEL)){//如果是非密文件
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
                try{
                    rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                }catch (Exception ex){
                    logger.error("聊天附件mq发送消息异常");
                    logger.error(common.getExceptionMessage(ex));
                }

            }

        //文件上传信息更新end
        //保存消息begin
        String receiver  = common.nulToEmptyString(common.getJsonStringKeyValue(message,"toId"));
        String sender = common.nulToEmptyString(common.getJsonStringKeyValue(message,"fromId"));
        messageInfo.setMsgId(msgId);
        messageInfo.setCreatetime(new Date());
        messageInfo.setLevels(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.secretLevel")));
        messageInfo.setReceiver(receiver);
        messageInfo.setSender(sender);
        messageInfo.setType(type);
        messageInfo.setIp(ip);
        messageInfo.setFrontId(common.nulToEmptyString(common.getJsonStringKeyValue(message,"id")));
        messageInfo.setContent(message);
        messageInfo.setFileType(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.type")));

        /*JSONArray atUsers = ((JSONArray) common.getJsonStringKeyValue(message,"atId"));
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
        messageInfo.setMsg(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.title")));
        messageInfo.setFileId(common.nulToEmptyString(common.getJsonStringKeyValue(message,"content.id")));
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
            String senderOrg = common.nulToEmptyString(senderInf.getOrgCode());
            String receiverOrg = common.nulToEmptyString(receiveInf.getOrgCode());
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
            zzContactInfSender.setAvartar(common.nulToEmptyString(common.getJsonStringKeyValue(message,"avatar")));
            zzContactInfSender.setName(common.nulToEmptyString(common.getJsonStringKeyValue(message,"username")));
            zzContactInfSender.setMemberNum("2");
            zzContactInfSender.setGroupOwner("");
            Map<String,String> param = new HashMap<>();
            param.put("userid",sender);
            UserInfo userSenderInf = this.iUserServiceUserService.getUserInfo(param);
            if(userSenderInf == null){
                userSenderInf = new UserInfo();
                common.putVoNullStringToEmptyString(userSenderInf);
            }
            zzContactInfSender.setLevels(userSenderInf.getSecretLevel());
            zzContactInfSender.setPid(userSenderInf.getPId());
            common.putVoNullStringToEmptyString(zzContactInfSender);
            this.zzContactService.add(zzContactInfSender);
        }
        String receiverCnt = this.zzContactService.countsById(receiver);
        //如果没有该联系人信息 接收人是单人，也有可能是群或者会议
        if("0".equals(receiverCnt)){
            zzContactInfReceiver.setId(receiver);
            zzContactInfReceiver.setType(type);
            zzContactInfReceiver.setName(common.nulToEmptyString(common.getJsonStringKeyValue(message,"toName")));
            if("GROUP".equals(type) || "MEET".equals(type)){
                zzContactInfReceiver.setAvartar(common.nulToEmptyString(common.getJsonStringKeyValue(message,"contactInfo.avatar")));
                zzContactInfReceiver.setLevels(common.nulToEmptyString(common.getJsonStringKeyValue(message,"contactInfo.secretLevel")));
                zzContactInfReceiver.setPid("");
                zzContactInfReceiver.setMemberNum(common.nulToEmptyString(common.getJsonStringKeyValue(message,"contactInfo.memberNum")));
                zzContactInfReceiver.setGroupOwner(common.nulToEmptyString(common.getJsonStringKeyValue(message,"contactInfo.groupOwnerId")));
            }else if("USER".equals(type)){
                Map<String,String> param = new HashMap<>();
                param.put("userid",receiver);
                UserInfo userReceiverInf = this.iUserServiceUserService.getUserInfo(param);
                if(userReceiverInf == null){
                    userReceiverInf = new UserInfo();
                    common.putVoNullStringToEmptyString(userReceiverInf);
                }
                zzContactInfReceiver.setLevels(userReceiverInf.getSecretLevel());
                zzContactInfReceiver.setPid(userReceiverInf.getPId());
                zzContactInfReceiver.setAvartar(userReceiverInf.getAvatar());
                zzContactInfSender.setGroupOwner("");
                zzContactInfReceiver.setMemberNum("2");
            }
            common.putVoNullStringToEmptyString(zzContactInfReceiver);
            this.zzContactService.add(zzContactInfReceiver);
        }
        //保存联系人信息end
        } catch (Exception e) {
            //异常记录到日志
            logger.error(common.getExceptionMessage(e));
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
    public MsgAnswerVO msgAnswer(String msg,String nId) throws Exception {
        return msgAnswer(msg,nId,SUCCESS_ANSWER,"发送成功");
    }
    //应答消息重载加入 状态 和 应答内容
    public MsgAnswerVO msgAnswer(String msg,String nId,int status,String content) throws Exception {
        MsgAnswerVO msgAnswerVO = new MsgAnswerVO();
        msgAnswerVO.setCode(MSG_ANSWER);
        msgAnswerVO.setContactId((String)getJsonStringKeyValue(msg,"data.toId"));
        msgAnswerVO.setnId(nId);
        msgAnswerVO.setStatus(status);
        msgAnswerVO.setContent(content);
        return  msgAnswerVO;
   }

}
