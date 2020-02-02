package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.entity.ZzGroupMsg;
import com.workhub.z.servicechat.model.HistoryMessageDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 群组消息表(ZzGroupMsg)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-10 11:38:02
 */
public interface ZzGroupMsgDao extends Mapper<ZzGroupMsg> {

    /**
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    ZzGroupMsg queryById(String msgId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzGroupMsg> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzGroupMsg 实例对象
     * @return 对象列表
     */
    List<ZzGroupMsg> queryAll(ZzGroupMsg zzGroupMsg);

    /**
     * 新增数据
     *
     * @param zzGroupMsg 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzGroupMsg zzGroupMsg);

    /**
     * 修改数据
     *
     * @param zzGroupMsg 实例对象
     * @return 影响行数
     */
    int update(ZzGroupMsg zzGroupMsg);

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 影响行数
     */
    int deleteById(String msgId);
    /**
     * 查询消息记录--最近
     *
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    List<ZzGroupMsg> queryMsgRecent(Map<String,String> param);
    /**
     * 查询消息记录--历史
     *
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    List<ZzGroupMsg> queryMsgHis(Map<String,String> param);
    /**
     * 查询消息记录--历史+最近
     *
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    List<ZzGroupMsg> queryMsgCurrentAndHis(Map<String,String> param);

    List<String> getReceiversByMsgId(@Param("msgId") String msgId);

    /**
    *@Description: 查询最近联系人历史消息
    *@Author: 忠
    *@date: 2019/6/22
    */

    List<HistoryMessageDto> queryHistoryMessageById(@Param("userId") String userId);
}