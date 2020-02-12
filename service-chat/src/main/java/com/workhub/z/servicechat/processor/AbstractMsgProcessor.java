package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.workhub.z.servicechat.VO.MessageSecretValidVo;
import com.workhub.z.servicechat.VO.MsgAnswerVO;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.*;
import com.workhub.z.servicechat.feign.IUserService;
import com.workhub.z.servicechat.model.MeetingDto;
import com.workhub.z.servicechat.server.IworkServerConfig;
import com.workhub.z.servicechat.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;

import java.util.Date;
import java.util.List;

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

    public WsResponse getWsResponse(String msg){
        return WsResponse.fromText(msg, IworkServerConfig.CHARSET);
    }
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
    */
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
            List<AdminUser> userInfoList = iUserServiceUserService.userList(sender+","+receiver);
            AdminUser senderInf = userInfoList.get(0)==null?new AdminUser():userInfoList.get(0);
            AdminUser receiveInf = userInfoList.get(1)==null?new AdminUser():userInfoList.get(1);
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

        /*UserInfo userInfo = iUserServiceUserService.info(sender);
        if(userInfo.getOrgCode()==null){
            userInfo.setOrgCode("");
            userInfo.setOrgName("");
        }
        messageInfo.setOrgCode(userInfo.getOrgCode());
        messageInfo.setOrgName(userInfo.getOrgName());
        */
        messageInfo.setOrgCode("");
        messageInfo.setOrgName("");
        messageInfoService.insert(messageInfo);
    }

    /**
    *@Description: 应答信息
    *@Param:消息内容
    *@return:
    *@Author: 忠
    *@date: 2019/7/30
    */
    // TODO: 2019/9/16 应答信息
    public void msgAnswer(String msg,String nId,ChannelContext channelContext) throws Exception {
        msgAnswer(msg,nId,channelContext,SUCCESS_ANSWER,"发送成功");
    }
    //应答消息重载加入 状态 和 应答内容
    public void msgAnswer(String msg,String nId,ChannelContext channelContext,int status,String content) throws Exception {
        MsgAnswerVO msgAnswerVO = new MsgAnswerVO();
        msgAnswerVO.setCode(MSG_ANSWER);
        msgAnswerVO.setContactId((String)getJsonStringKeyValue(msg,"data.toId"));
        msgAnswerVO.setnId(nId);
        msgAnswerVO.setoId((String)getJsonStringKeyValue(msg,"data.id"));
        msgAnswerVO.setStatus(status);
        msgAnswerVO.setContent(content);
//        System.out.println((String)getJsonStringKeyValue(msg,"data.fromId"));
        Tio.sendToUser(channelContext.getGroupContext(),(String)getJsonStringKeyValue(msg,"data.fromId"),this.getWsResponse(JSON.toJSONString(msgAnswerVO, SerializerFeature.DisableCircularReferenceDetect)));
    }

}
