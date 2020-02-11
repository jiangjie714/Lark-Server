package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.dao.ZzMessageInfoDao;
import com.workhub.z.servicechat.entity.ZzMessageInfo;
import com.workhub.z.servicechat.model.ContactsMessageDto;
import com.workhub.z.servicechat.model.RawMessageDto;
import com.workhub.z.servicechat.service.ZzMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.workhub.z.servicechat.config.common.aggregation;
import static com.workhub.z.servicechat.config.common.putEntityNullToEmptyString;

/**
 * 消息存储(ZzMessageInfo)表服务实现类
 *
 * @author makejava
 * @since 2019-06-23 13:50:41
 */
@Service("zzMessageInfoService")
public class ZzMessageInfoServiceImpl implements ZzMessageInfoService {
    private static Logger log = LoggerFactory.getLogger(ZzMessageInfoServiceImpl.class);
    @Resource
    private ZzMessageInfoDao zzMessageInfoDao;
    /**
     * 新增数据
     *
     * @param zzMessageInfo 实例对象
     * @return 实例对象
     */
    @Override
    public ZzMessageInfo insert(ZzMessageInfo zzMessageInfo) {
        try {
            putEntityNullToEmptyString(zzMessageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.zzMessageInfoDao.insert(zzMessageInfo);
        return zzMessageInfo;
    }

    /**
     * 修改数据
     *
     * @param zzMessageInfo 实例对象
     * @return 实例对象
     */
    @Override
    public int update(ZzMessageInfo zzMessageInfo) {
        return  this.zzMessageInfoDao.update(zzMessageInfo);
    }

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String msgId) {
        return this.zzMessageInfoDao.deleteById(msgId) > 0;
    }
    /**
     *@Description: 查询最近联系人
     *@Param:
     *@return:
     *@Author: 忠
     *@date: 2019/6/23
     */
    @Override
    public String queryContactsMessage2(String userId){
        List<ContactsMessageDto> contactsMessageDtoList =this.zzMessageInfoDao.queryContactsMessage2(userId);

        List<List<ContactsMessageDto>> list2 = aggregation(contactsMessageDtoList, new Comparator<ContactsMessageDto>() {

            @Override
            public int compare(ContactsMessageDto o1, ContactsMessageDto o2) {
//                return o1.getContactsId() == o2.getContactsId() ? 0:-1;
                //按联系人分组
                return o1.getContactsId().compareTo(o2.getContactsId());

            }
        });
        List<List<ContactsMessageDto>> list3 = list2;
        String s = JSON.toJSONString(list2);
        String s1 = "[";
        for (int j = 0; j < list2.size(); j++) {
            s1 +="[";
            for (int i = 0; i < list2.get(j).size(); i++) {
                if (i == 0){
                    s1 += "\"";
                    s1 += list2.get(j).get(i).getContactsId();
                    s1 +="\",";
                }
                s1 += list2.get(j).get(i).getContent();

                if (i==list2.get(j).size()-1){ }
                else {
                    s1 += ",";
                }
            }
            if (j==list2.size()-1){
                s1 +="]";
            }else {
                s1 +="],";
            }
        }
        s1 +="]";
        return  s1;
    }
    /**
    *@Description: 查询最近联系人
    *@Param:
    *@return:
    *@Author: 忠
    *@date: 2019/6/23
    */
    @Override
    public String queryContactsMessage(String userId){
        List<RawMessageDto> contactsMessageDtoList =this.zzMessageInfoDao.queryContactsMessage(userId);

        List<List<RawMessageDto>> list2 = aggregation(contactsMessageDtoList, new Comparator<RawMessageDto>() {

            @Override
            public int compare(RawMessageDto o1, RawMessageDto o2) {
                //按联系人分组
                return o1.getContactsid().compareTo(o2.getContactsid());

            }
        });
        String s1 = "[";
        for (int j = 0; j < list2.size(); j++) {
             s1 +="[";
             for (int i = 0; i < list2.get(j).size(); i++) {
             if (i == 0){
                 s1 += "\"";
                 s1 += list2.get(j).get(i).getContactsid();
                 s1 +="\",";
                  }
              NewMessageVo msg = this.rawMsgToMsgVo(list2.get(j).get(i)) ;
             s1 += JSON.toJSONString(msg);

             if (i==list2.get(j).size()-1){ }
              else {
                   s1 += ",";
              }
         }
                    if (j==list2.size()-1){
                        s1 +="]";
                    }else {
                        s1 +="],";
                    }
                }
        s1 +="]";
        return  s1;
    }
    /**
     * @MethodName: queryMessageList
     * @Description: 查询群主，私人，会议消息记录
     * @Param: [type 类型 ：USER、GROUP、MEET, receiver 接收人id]
     * @Return: java.lang.String
     * @Author: zhuqz
     * @Date: 2019/10/18
     **/
    @Override
    public String queryMessageList2(String type,String receiver,String userId){
        List<ContactsMessageDto> contactsMessageDtoList =this.zzMessageInfoDao.queryMessageList2(type,receiver,userId);
        String s1 = "[";
        for (int i = 0; i < contactsMessageDtoList.size(); i++) {
            if (i == 0){
                s1 += "\"";
                s1 += contactsMessageDtoList.get(i).getContactsId();
                s1 +="\",";
            }
            s1 += contactsMessageDtoList.get(i).getContent();
            if (i==contactsMessageDtoList.size()-1){ }
            else {
                s1 += ",";
            }
        }
        s1 +="]";
        return  s1;
    }
    /**
    * @MethodName: queryMessageList
     * @Description: 查询群主，私人，会议消息记录
     * @Param: [type 类型 ：USER、GROUP、MEET, receiver 接收人id]
     * @Return: java.lang.String
     * @Author: zhuqz
     * @Date: 2019/10/18
    **/
    @Override
    public String queryMessageList(String type,String receiver,String userId){
        List<RawMessageDto> contactsMessageDtoList =this.zzMessageInfoDao.queryMessageList(type,receiver,userId);
        String s1 = "[";
            for (int i = 0; i < contactsMessageDtoList.size(); i++) {
                NewMessageVo msgVo = this.rawMsgToMsgVo(contactsMessageDtoList.get(i));
                if (i == 0){
                    s1 += "\"";
                    s1 += contactsMessageDtoList.get(i).getContactsid();
                    s1 +="\",";
                }
                s1 += JSON.toJSONString(msgVo);
                if (i==contactsMessageDtoList.size()-1){ }
                else {
                    s1 += ",";
                }
            }
        s1 +="]";
        return  s1;
    }


    @Override
    public TableResultResponse queryHistoryMessageForSingle2(String userId, String contactId, String isGroup, String query, String page, String size){
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(page);
            pageSize=Integer.valueOf(size);
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<String> dataList=null;
        if("true".equals(isGroup)){
            dataList=this.zzMessageInfoDao.queryHistoryMessageForSingleGroup2(userId,contactId,query);
        }else if("false".equals(isGroup)){
            dataList=this.zzMessageInfoDao.queryHistoryMessageForSinglePrivate2(userId,contactId,query);
        }else if("meet".equals(isGroup)){
            dataList=this.zzMessageInfoDao.queryHistoryMessageForSingleMeet2(userId,contactId,query);
        }
        SimpleDateFormat fullf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dayf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat shortTimef = new SimpleDateFormat("HH:mm");
        SimpleDateFormat shortDayf = new SimpleDateFormat("MM-dd");
        PageInfo pageInfo = new PageInfo<>(dataList);
        List<SingleMessageVO> voList=new ArrayList<>();
        for(String temp:dataList){
            SingleMessageVO vo = JSON.parseObject(temp, SingleMessageVO.class);
            Date time = vo.getTime();//发送时间
            String shortTime = "";//当天显示十分，昨天以前显示日期
            String fullTime = "";//全日期
            if(time!=null){
                fullTime=fullf.format(time);
                if(dayf.format(time).equals(dayf.format(new Date()))){//格式化为相同格式
                    shortTime=shortTimef.format(time);
                }else {
                    shortTime=shortDayf.format(time);
                }
            }
            vo.setSendTimeShort(shortTime);
            vo.setSendTimeFull(fullTime);
            voList.add(vo);
        }
        try {
            common.putVoNullStringToEmptyString(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }

        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                voList
        );

        return  res;
    }
    //query 聊天内容
    //pageSize条数,pageNum页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
    @Override
    public TableResultResponse queryHistoryMessageForSingle(String userId, String contactId, String isGroup, String query, String page, String size){
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(page);
            pageSize=Integer.valueOf(size);
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RawMessageDto> dataList=null;
        if("true".equals(isGroup)){
            dataList=this.zzMessageInfoDao.queryHistoryMessageForSingleGroup(userId,contactId,query);
        }else if("false".equals(isGroup)){
            dataList=this.zzMessageInfoDao.queryHistoryMessageForSinglePrivate(userId,contactId,query);
        }else if("meet".equals(isGroup)){
            dataList=this.zzMessageInfoDao.queryHistoryMessageForSingleMeet(userId,contactId,query);
        }
        SimpleDateFormat fullf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dayf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat shortTimef = new SimpleDateFormat("HH:mm");
        SimpleDateFormat shortDayf = new SimpleDateFormat("MM-dd");
        PageInfo pageInfo = new PageInfo<>(dataList);
        List<NewSingleMessageVO> voList=new ArrayList<>();
        for(RawMessageDto dto:dataList){
            NewMessageVo vo1= this.rawMsgToMsgVo(dto);
            NewSingleMessageVO vo = new NewSingleMessageVO();
            try {
                common.copyObject(vo1,vo);
            } catch (Exception e) {
                log.error("消息转换错误");
                log.error(common.getExceptionMessage(e));
            }
            Date time = new Date();
            try {
                time = fullf.parse(vo.getSendTime());
            } catch (ParseException e) {
                log.error("消息日期转换错误");
                log.error(common.getExceptionMessage(e));
            }
            String shortTime = "";//当天显示十分，昨天以前显示日期
            if(time!=null){
                if(dayf.format(time).equals(dayf.format(new Date()))){//格式化为相同格式
                    shortTime=shortTimef.format(time);
                }else {
                    shortTime=shortDayf.format(time);
                }
            }
            vo.setSendTimeShort(shortTime);
            voList.add(vo);
        }
        try {
            common.putVoNullStringToEmptyString(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }

        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                voList
        );

        return  res;
    }
    //监控消息-私聊
    // pageSize条数,pageNo页码,timeEnd开始时间,timeBegin结束时间,senderName发送人,receiverName接收人,messageLevel消息密级，messageContent内容
    @Override
    public TableResultResponse queryAllMessagePrivate2(Map params) throws Exception{
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        String sendTimeQuery= common.nulToEmptyString(params.get("sendTime"));
        if(!"".equals(sendTimeQuery)){
            params.put("timeBegin",sendTimeQuery.toString().split(",")[0]);
            params.put("timeEnd",sendTimeQuery.toString().split(",")[1]);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,String>> dataList=null;

        dataList=this.zzMessageInfoDao.queryAllMessagePrivate2(params);


        PageInfo pageInfo = new PageInfo<>(dataList);
        List<MessageMonitoringPrivateVo> voList=new ArrayList<>();
        for(Map<String,String> map:dataList){
            String temp = map.get("CONTENT");
            MessageMonitoringPrivateVo vo = new MessageMonitoringPrivateVo();
            String senderName = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"username"));
            vo.setSenderName(senderName);
            String receiverId = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"toId"));
            vo.setReceiverId(receiverId);
            String senderId = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"fromId"));
            vo.setSenderId(senderId);
            String ip = map.get("IP");
            vo.setIp(ip);
            String id = map.get("MSG_ID");
            vo.setId(id);
            String senderSN = map.get("SENDER_SN");
            vo.setSenderSn(senderSN);
            vo.setStatus("成功");
            String senderLevel = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"contactInfo.secretLevel"));
            vo.setSenderLevel(senderLevel);
            String sendTime = map.get("SENDTIME");
            vo.setSendTime(sendTime);
            String messageLevel = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"content.secretLevel"));
            vo.setMessageLevel(messageLevel);
            String receiverName = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"toName"));
            vo.setReceiverName(receiverName);
            String messageContent = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"content.title"));
            vo.setMessageContent(messageContent);
            vo.setId(map.get("ID"));
            voList.add(vo);
        }
        try {
            common.putVoNullStringToEmptyString(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }

        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                voList
        );

        return  res;
    }
    //监控消息-私聊
    // pageSize条数,pageNo页码,timeEnd开始时间,timeBegin结束时间,senderName发送人,receiverName接收人,messageLevel消息密级，messageContent内容
    @Override
    public TableResultResponse queryAllMessageMonitor(Map params) throws Exception{
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        String sendTimeQuery= common.nulToEmptyString(params.get("sendTime"));
        if(!"".equals(sendTimeQuery)){
            params.put("timeBegin",sendTimeQuery.toString().split(",")[0]);
            params.put("timeEnd",sendTimeQuery.toString().split(",")[1]);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RawMessageDto> dataList=null;

        dataList=this.zzMessageInfoDao.queryAllMessageMonitor(params);


        PageInfo pageInfo = new PageInfo<>(dataList);
        List<MessageMonitoringPrivateVo> voList=new ArrayList<>();
        for(RawMessageDto dto:dataList){
            MessageMonitoringPrivateVo vo = this.rawMsgToMonitorVo(dto);
            voList.add(vo);
        }
        try {
            common.putVoNullStringToEmptyString(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }

        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                voList
        );

        return  res;
    }
    @Override
    public TableResultResponse queryAllMessageGroup2(Map params) throws Exception{
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        String sendTimeQuery= common.nulToEmptyString(params.get("sendTime"));
        if(!"".equals(sendTimeQuery)){
            params.put("timeBegin",sendTimeQuery.toString().split(",")[0]);
            params.put("timeEnd",sendTimeQuery.toString().split(",")[1]);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,String>> dataList=null;

        dataList=this.zzMessageInfoDao.queryAllMessageGroup2(params);


        PageInfo pageInfo = new PageInfo<>(dataList);
        List<MessageMonitoringPrivateVo> voList=new ArrayList<>();
        for(Map<String,String> map:dataList){
            String temp = map.get("CONTENT");
            MessageMonitoringPrivateVo vo = new MessageMonitoringPrivateVo();
            String senderName = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"username"));
            vo.setSenderName(senderName);
            String senderLevel = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"contactInfo.secretLevel"));
            vo.setSenderLevel(senderLevel);
            String receiverId = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"toId"));
            vo.setReceiverId(receiverId);
            String senderId = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"fromId"));
            vo.setSenderId(senderId);
            String receiverName = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"toName"));
            vo.setReceiverName(receiverName);
            String sendTime = map.get("SENDTIME");
            String ip = map.get("IP");
            vo.setIp(ip);
            String id = map.get("MSG_ID");
            vo.setId(id);
            String senderSN = map.get("SENDER_SN");
            vo.setSenderSn(senderSN);
            vo.setStatus("成功");
            vo.setSendTime(sendTime);
            String messageLevel = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"content.secretLevel"));
            vo.setMessageLevel(messageLevel);
            String messageContent = common.nulToEmptyString(common.getJsonStringKeyValue(temp,"content.title"));
            vo.setMessageContent(messageContent);
            vo.setId(map.get("ID"));
            voList.add(vo);
        }
        try {
            common.putVoNullStringToEmptyString(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }

        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                voList
        );

        return  res;
    }


    /**
     * 原始消息转换给前端消息体
     * @param rawDto
     * @return
     */

    public NewMessageVo rawMsgToMsgVo(RawMessageDto rawDto){
        NewMessageVo msg = new NewMessageVo();
        msg.setId(common.nulToEmptyString(rawDto.getMsgId()));
        msg.setSendTime(common.nulToEmptyString(rawDto.getCreatetime()));
        //msg.setTeamType(common.nulToEmptyString(rawDto.getType()));
        //msg.setCross(common.nulToEmptyString(rawDto.getCross()));

        NewContactVo sender = new NewContactVo();
        sender.setId(common.nulToEmptyString(rawDto.getSenderid()));
        sender.setName(common.nulToEmptyString(rawDto.getSendername()));
        sender.setAvartar(common.nulToEmptyString(rawDto.getSenderavatar()));
        sender.setLevels(common.nulToEmptyString(rawDto.getSenderlevels()));
        sender.setPid(common.nulToEmptyString(rawDto.getSenderpid()));

        /*try {
            common.putVoNullStringToEmptyString(sender);
        } catch (Exception e) {
            log.error("消息转换出错");
        }*/
        msg.setSender(sender);

        NewContactVo contactor = new NewContactVo();
        contactor.setId(common.nulToEmptyString(rawDto.getReceiverid()));
        contactor.setName(common.nulToEmptyString(rawDto.getReceivername()));
        contactor.setAvartar(common.nulToEmptyString(rawDto.getReceiveravatar()));
        contactor.setLevels(common.nulToEmptyString(rawDto.getReceiverlevels()));
        contactor.setPid(common.nulToEmptyString(rawDto.getReceiverpid()));
        contactor.setGroupOwner(common.nulToEmptyString(rawDto.getReceivergroupowner()));
        contactor.setMemberNum(common.nulToEmptyString(rawDto.getReceivermembernum()));
        /*try {
            common.putVoNullStringToEmptyString(contactor);
        } catch (Exception e) {
            log.error("消息转换出错");
        }*/
        msg.setContactor(contactor);

        NewContentVo contentVo = new NewContentVo();
        contentVo.setTitle(common.nulToEmptyString(rawDto.getMsg()));
        contentVo.setType(common.nulToEmptyString(rawDto.getFiletype()));
        contentVo.setSecretLevel(common.nulToEmptyString(rawDto.getLevels()));
        contentVo.setExtension(common.nulToEmptyString(rawDto.getFileext()));
        contentVo.setFileSize(common.nulToEmptyString(rawDto.getFilesize()));
        contentVo.setId(common.nulToEmptyString(rawDto.getFileid()));
        /*try {
            common.putVoNullStringToEmptyString(contentVo);
        } catch (Exception e) {
            log.error("消息转换出错");
        }*/
        msg.setMsgContent(contentVo);

        /*try {
            common.putVoNullStringToEmptyString(msg);
        } catch (Exception e) {
            log.error("消息转换出错");
        }*/

        return msg;
    }

    /**
     * 原始消息转换给列表消息体
     * @param rawDto
     * @return
     */

    public SingleMessageVO rawMsgToSingleVo(RawMessageDto rawDto){
        SingleMessageVO msg = new SingleMessageVO();
        msg.setId(common.nulToEmptyString(rawDto.getMsgId()));
        msg.setUsername(common.nulToEmptyString(rawDto.getSendername()));
        msg.setAvatar(common.nulToEmptyString(rawDto.getSenderavatar()));
        msg.setFromId(common.nulToEmptyString(rawDto.getSenderid()));
        msg.setToId(common.nulToEmptyString(rawDto.getReceiverid()));
        msg.setSecretLevel(Integer.parseInt(common.nulToZeroString(rawDto.getLevels())));
        msg.setType(Integer.parseInt(common.nulToZeroString(rawDto.getFiletype())));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            msg.setTime(format.parse(rawDto.getCreatetime()));
        } catch (ParseException e) {
            log.error("日期转换错误");
            log.error(common.getExceptionMessage(e));
        }
        msg.setGroup(rawDto.getType().equals("GROUP") || rawDto.getType().equals("MEET"));
        ContentVO contentVO = new ContentVO();
        contentVO.setExtension(common.nulToEmptyString(rawDto.getFileext()));
        contentVO.setId(common.nulToEmptyString(rawDto.getFileid()));
        contentVO.setSecretLevel(common.nulToEmptyString(rawDto.getLevels()));
        contentVO.setTitle(common.nulToEmptyString(rawDto.getMsg()));
        contentVO.setType(Integer.parseInt(common.nulToZeroString(rawDto.getFiletype())));
        contentVO.setUrl("");
        msg.setContent(contentVO);
        return msg;
    }

    /**
     * dto 转 监控消息vo
     * @param rawDto
     * @return
     */
    public MessageMonitoringPrivateVo rawMsgToMonitorVo(RawMessageDto rawDto){
        MessageMonitoringPrivateVo vo = new MessageMonitoringPrivateVo();
        String senderName = common.nulToEmptyString(rawDto.getSendername());
        vo.setSenderName(senderName);
        String receiverId = common.nulToEmptyString(rawDto.getReceiverid());
        vo.setReceiverId(receiverId);
        String senderId = common.nulToEmptyString(rawDto.getSenderid());
        vo.setSenderId(senderId);
        String ip = common.nulToEmptyString(rawDto.getIp());
        vo.setIp(ip);
        String id = common.nulToEmptyString(rawDto.getMsgId());
        vo.setId(id);
        String senderSN = common.nulToEmptyString(rawDto.getSenderpid());
        vo.setSenderSn(senderSN);
        vo.setStatus("成功");
        String senderLevel = common.nulToEmptyString(rawDto.getSenderlevels());
        vo.setSenderLevel(senderLevel);
        String sendTime = common.nulToEmptyString(rawDto.getCreatetime());
        vo.setSendTime(sendTime);
        String messageLevel = common.nulToEmptyString(rawDto.getLevels());
        vo.setMessageLevel(messageLevel);
        String receiverName = common.nulToEmptyString(rawDto.getReceivername());
        vo.setReceiverName(receiverName);
        String messageContent = common.nulToEmptyString(rawDto.getMsg());
        vo.setMessageContent(messageContent);
        return  vo;
    }
}