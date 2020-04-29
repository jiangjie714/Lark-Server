package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.ExportMsgVo;
import com.workhub.z.servicechat.entity.message.ZzMessageInfo;
import com.workhub.z.servicechat.model.ContactsMessageDto;
import com.workhub.z.servicechat.model.RawMessageDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 消息存储(ZzMessageInfo)表数据库访问层
 *
 * @author makejava
 * @since 2019-06-23 13:50:41
 */
public interface ZzMessageInfoDao {
    /**
     * 新增数据
     *
     * @param zzMessageInfo 实例对象
     * @return 影响行数
     */
    int insert(ZzMessageInfo zzMessageInfo);

    /**
     * 修改数据
     *
     * @param zzMessageInfo 实例对象
     * @return 影响行数
     */
    int update(ZzMessageInfo zzMessageInfo);

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 影响行数
     */
    int deleteById(String msgId);
    /**
     * 通过主键查询数据
     *
     * @param msgId 主键
     */
    ZzMessageInfo queryById(String msgId);

    List<ContactsMessageDto> queryContactsMessage2(@Param("userId")String userId);
    List<RawMessageDto> queryContactsMessage(@Param("userId")String userId);
   /**
   *
    * 查询会议或者群消息列表
    * @param type 类型 GROUP群 USER 私聊 MEET 会议，
    * @param receiver 接收人
    * @return: List<ContactsMessageDto>
    * @Author: zhuqz
    * @Date: 2019/10/18
   **/
   List<ContactsMessageDto> queryMessageList2(@Param("type")String type,@Param("receiver")String receiver,@Param("userId")String userId);
   List<RawMessageDto> queryMessageList(@Param("type")String type,@Param("receiver")String receiver,@Param("userId")String userId);

   List<String> queryHistoryMessageForSinglePrivate2(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
   List<String> queryHistoryMessageForSingleGroup2(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
    List<String> queryHistoryMessageForSingleMeet2(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);

    //todo 给前端结构变更
    List<RawMessageDto> queryHistoryMessageForSinglePrivate(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
    List<RawMessageDto> queryHistoryMessageForSingleGroup(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
    List<RawMessageDto> queryHistoryMessageForSingleMeet(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
    //导出消息
    List<ExportMsgVo> exportHistoryMessageForSinglePrivate(@Param("userId")String userId, @Param("contactId")String contactId, @Param("beginDate")String beginDate, @Param("endDate")String endDate);
    List<ExportMsgVo> exportHistoryMessageForSingleGroup(@Param("userId")String userId,@Param("contactId")String contactId,@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    List<ExportMsgVo> exportHistoryMessageForSingleMeet(@Param("userId")String userId,@Param("contactId")String contactId,@Param("beginDate")String beginDate,@Param("endDate")String endDate);

   List<Map<String,String>> queryAllMessagePrivate2(@Param("params")Map params);
   List<Map<String,String>> queryAllMessageGroup2(@Param("params")Map params);
   List<RawMessageDto> queryAllMessageMonitor(@Param("params")Map params);
}