package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.MessageMonitoringPrivateVo;
import com.workhub.z.servicechat.VO.SingleMessageVO;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.dao.message.ZzMessageInfoDao;
import com.workhub.z.servicechat.entity.message.ZzMessageInfo;
import com.workhub.z.servicechat.model.ContactsMessageDto;
import com.workhub.z.servicechat.service.ZzMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    @Override
    public ZzMessageInfo queryById(String msgId) {
        ZzMessageInfo zzMessageInfo = this.zzMessageInfoDao.queryById(msgId);
        try {
            common.putVoNullStringToEmptyString(zzMessageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        return zzMessageInfo;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzMessageInfo> queryAllByLimit(int offset, int limit) {
        return this.zzMessageInfoDao.queryAllByLimit(offset, limit);
    }

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
    public ZzMessageInfo update(ZzMessageInfo zzMessageInfo) {
        this.zzMessageInfoDao.update(zzMessageInfo);
        return this.queryById(zzMessageInfo.getMsgId());
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
    public String queryContactsMessage(String userId){
        List<ContactsMessageDto> contactsMessageDtoList =this.zzMessageInfoDao.queryContactsMessage(userId);

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
    * @MethodName: queryMessageList
     * @Description: 查询群主，私人，会议消息记录
     * @Param: [type 类型 ：USER、GROUP、MEET, receiver 接收人id]
     * @Return: java.lang.String
     * @Author: zhuqz
     * @Date: 2019/10/18
    **/
    @Override
    public String queryMessageList(String type,String receiver,String userId){
        List<ContactsMessageDto> contactsMessageDtoList =this.zzMessageInfoDao.queryMessageList(type,receiver,userId);
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
        List<String> dataList=null;
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
    //监控消息-私聊
    // pageSize条数,pageNo页码,timeEnd开始时间,timeBegin结束时间,senderName发送人,receiverName接收人,messageLevel消息密级，messageContent内容
    @Override
    public TableResultResponse queryAllMessagePrivate(Map params) throws Exception{
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

        dataList=this.zzMessageInfoDao.queryAllMessagePrivate(params);


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
    //监控消息-群聊
    // pageSize条数,pageNo页码,timeEnd开始时间,timeBegin结束时间,senderName发送人,receiverName接收人,messageLevel消息密级，messageContent内容
    @Override
    public TableResultResponse queryAllMessageGroup(Map params) throws Exception{
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

        dataList=this.zzMessageInfoDao.queryAllMessageGroup(params);


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
}