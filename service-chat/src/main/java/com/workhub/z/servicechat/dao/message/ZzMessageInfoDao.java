package com.workhub.z.servicechat.dao.message;

import com.workhub.z.servicechat.entity.message.ZzMessageInfo;
import com.workhub.z.servicechat.model.ContactsMessageDto;
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
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    ZzMessageInfo queryById(String msgId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMessageInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzMessageInfo 实例对象
     * @return 对象列表
     */
    List<ZzMessageInfo> queryAll(ZzMessageInfo zzMessageInfo);

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

   List<ContactsMessageDto> queryContactsMessage(@Param("userId")String userId);
   /**
   *
    * 查询会议或者群消息列表
    * @param type 类型 GROUP群 USER 私聊 MEET 会议，
    * @param receiver 接收人
    * @return: List<ContactsMessageDto>
    * @Author: zhuqz
    * @Date: 2019/10/18
   **/
   List<ContactsMessageDto> queryMessageList(@Param("type")String type,@Param("receiver")String receiver,@Param("userId")String userId);
   List<String> queryHistoryMessageForSinglePrivate(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
   List<String> queryHistoryMessageForSingleGroup(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
    List<String> queryHistoryMessageForSingleMeet(@Param("userId")String userId,@Param("contactId")String contactId,@Param("query")String query);
   List<Map<String,String>> queryAllMessagePrivate(@Param("params")Map params);
   List<Map<String,String>> queryAllMessageGroup(@Param("params")Map params);
}