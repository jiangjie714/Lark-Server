package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.workhub.z.servicechat.VO.ChatAdminUserVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.MsgSyncDao;
import com.workhub.z.servicechat.dao.ZzContactDao;
import com.workhub.z.servicechat.entity.ZzContactInf;
import com.workhub.z.servicechat.entity.message.ZzMessageInfo;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.MsgSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/2/28 10:30
 * @description: 消息同步
 */
@Service("msgSyncService")
public class MsgSyncServiceImpl implements MsgSyncService {
    private static Logger log = LoggerFactory.getLogger(MsgSyncServiceImpl.class);
    //同步进行中
    private volatile static  boolean synGoing = false;
    //同步暂停
    private volatile static boolean stopFlg = false;
    @Autowired
    MsgSyncDao msgSyncDao;
    @Autowired
    ZzContactDao zzContactDao;
    @Autowired
    AdminUserService iUserService;

    /**
     * 0同步出错
     * 1同步完成
     * 2同步暂停
     * @return
     */
    @Override
    public int syncMsg() {
        if(synGoing){
            stopFlg =true;
            log.info("暂停同步消息-----------------------------");
            return 2;
        }
        synGoing =true;
        log.info("开始同步消息-----------------------------");
        ZzMessageInfo zzMessageInfo =null;
        String sender ;
        String receiver;
        String message ;
        String type ;
        ZzContactInf zzContactInfSender ;
        ZzContactInf zzContactInfReceiver ;
        try {
            //获取同步消息列表
            List<ZzMessageInfo> syncList = this.msgSyncDao.getSyncMsgList();
            while (!stopFlg && syncList!=null && syncList.size()!=0 ) {
                for (int i = 0; i < syncList.size(); i++) {
                     zzMessageInfo = syncList.get(i);
                     sender = zzMessageInfo.getSender();
                     receiver = zzMessageInfo.getReceiver();
                     message = zzMessageInfo.getContent();
                     //会议、群、私聊
                     type = zzMessageInfo.getType();
                    //解析消息，把json数据放到字段里
                    zzMessageInfo.setFrontId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"id")));
                    zzMessageInfo.setFileType(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.type")));
                    zzMessageInfo.setMsg(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.title")));
                    zzMessageInfo.setFileId(Common.nulToEmptyString(Common.getJsonStringKeyValue(message,"content.id")));
                    //同步消息到数据库
                    this.msgSyncDao.syncMsg(zzMessageInfo);
                    //设置同步标记
                    this.msgSyncDao.updateSyncFlg(zzMessageInfo.getMsgId());
                    //处理联系人
                    String senderCnt = this.zzContactDao.countsById(sender);
                    //如果没有该联系人信息 发送人一个是单个人
                    if ("0".equals(senderCnt)) {
                        zzContactInfSender = new ZzContactInf();
                        zzContactInfSender.setId(sender);
                        zzContactInfSender.setType("USER");
                        zzContactInfSender.setAvartar(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "avatar")));
                        zzContactInfSender.setName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "username")));
                        zzContactInfSender.setMemberNum("2");
                        zzContactInfSender.setGroupOwner("");
                        ChatAdminUserVo userSenderInf = this.iUserService.getUserInfo(sender);
                        if (userSenderInf == null) {
                            userSenderInf = new ChatAdminUserVo();
                            Common.putVoNullStringToEmptyString(userSenderInf);
                        }
                        zzContactInfSender.setLevels(userSenderInf.getSecretLevel());
                        zzContactInfSender.setPid(userSenderInf.getPId());
                        Common.putVoNullStringToEmptyString(zzContactInfSender);
                        this.zzContactDao.add(zzContactInfSender);
                    }
                    String receiverCnt = this.zzContactDao.countsById(receiver);
                    //如果没有该联系人信息 接收人是单人，也有可能是群或者会议
                    if ("0".equals(receiverCnt)) {
                        zzContactInfReceiver = new ZzContactInf();
                        zzContactInfReceiver.setId(receiver);
                        zzContactInfReceiver.setType(type);
                        zzContactInfReceiver.setName(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "toName")));
                        if ("GROUP".equals(type) || "MEET".equals(type)) {
                            zzContactInfReceiver.setAvartar(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "contactInfo.avatar")));
                            zzContactInfReceiver.setLevels(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "contactInfo.secretLevel")));
                            zzContactInfReceiver.setPid("");
                            zzContactInfReceiver.setMemberNum(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "contactInfo.memberNum")));
                            zzContactInfReceiver.setGroupOwner(Common.nulToEmptyString(Common.getJsonStringKeyValue(message, "contactInfo.groupOwnerId")));
                        } else if ("USER".equals(type)) {
                            ChatAdminUserVo userReceiverInf = this.iUserService.getUserInfo(receiver);
                            if (userReceiverInf == null) {
                                userReceiverInf = new ChatAdminUserVo();
                                Common.putVoNullStringToEmptyString(userReceiverInf);
                            }
                            zzContactInfReceiver.setLevels(userReceiverInf.getSecretLevel());
                            zzContactInfReceiver.setPid(userReceiverInf.getPId());
                            zzContactInfReceiver.setAvartar(userReceiverInf.getAvatar());
                            zzContactInfReceiver.setGroupOwner("");
                            zzContactInfReceiver.setMemberNum("2");
                        }
                        Common.putVoNullStringToEmptyString(zzContactInfReceiver);
                        this.zzContactDao.add(zzContactInfReceiver);

                    }

                }
                //同步下一个批次
                syncList = this.msgSyncDao.getSyncMsgList();
            }
            if(stopFlg){
                log.info("暂停同步消息-----------------------------");
                return 2;
            }
        } catch (Exception e) {
            log.error("同步消息出错-----------------------------");
            if(zzMessageInfo!=null && zzMessageInfo.getMsgId()!=null){
                log.error("msg id is "+zzMessageInfo.getMsgId()+"-----------------------------");
            }
            log.error(Common.getExceptionMessage(e));
            return 0;
        }finally {
            synGoing = false;
            stopFlg = false;
        }
        log.info("结束同步消息 一切正常-----------------------------");
        return 1;
    }
}