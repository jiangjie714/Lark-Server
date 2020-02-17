package com.workhub.z.servicechat.dao.message;

import com.workhub.z.servicechat.VO.PrivateFileVO;
import com.workhub.z.servicechat.entity.message.ZzPrivateMsg;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 私人消息(ZzPrivateMsg)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
public interface ZzPrivateMsgDao extends Mapper<ZzPrivateMsg> {

    /**
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    ZzPrivateMsg queryById(String msgId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzPrivateMsg> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzPrivateMsg 实例对象
     * @return 对象列表
     */
    List<ZzPrivateMsg> queryAll(ZzPrivateMsg zzPrivateMsg);

    /**
     * 新增数据
     *
     * @param zzPrivateMsg 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzPrivateMsg zzPrivateMsg);

    /**
     * 修改数据
     *
     * @param zzPrivateMsg 实例对象
     * @return 影响行数
     */
    int update(ZzPrivateMsg zzPrivateMsg);

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
    List<ZzPrivateMsg> queryMsgRecent(Map<String,String> param);
    /**
     * 查询消息记录--历史
     *
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    List<ZzPrivateMsg> queryMsgHis(Map<String,String> param);
    /**
     * 查询消息记录--历史+最近
     *
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    List<ZzPrivateMsg> queryMsgCurrentAndHis(Map<String,String> param);
    //查询私有聊天文件
    //query文件名称
    List<PrivateFileVO> getFileList(@Param("userId") String userId,@Param("receiverId") String receiverId,@Param("query") String query);
}