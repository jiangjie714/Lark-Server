package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.PrivateFileVO;
import com.workhub.z.servicechat.entity.ZzPrivateMsg;

import java.util.List;
import java.util.Map;

/**
 * 私人消息(ZzPrivateMsg)表服务接口
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
public interface ZzPrivateMsgService {

    /**
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    ZzPrivateMsg queryById(String msgId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzPrivateMsg> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzPrivateMsg 实例对象
     * @return 实例对象
     */
    void insert(ZzPrivateMsg zzPrivateMsg);

    /**
     * 修改数据
     *
     * @param zzPrivateMsg 实例对象
     * @return 实例对象
     */
    Integer update(ZzPrivateMsg zzPrivateMsg);

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 是否成功
     */
    boolean deleteById(String msgId);

    // TODO: 2019/5/15 msgSender msgReceiver查询消息列表

    // TODO: 2019/5/15 根据 msgSender msgReceiver更新未读消息isRead

    // TODO: 2019/5/15  
    /**
     * 查询消息记录
     * @auther zhuqz
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    TableResultResponse<ZzPrivateMsg> queryMsg(Map<String,String> param) throws Exception;
    
    /**
    *@Description: 根据消息ID查询发送人详细信息
    *@Param: 
    *@return: userid
    *@Author: 忠
    *@date: 2019/6/20
    */
    String getSenderByMsgId(String msgId) throws Exception;
    
    /**
    *@Description: 根据消息ID查询接收人详细信息（若为群组则返回当前群组内userList）
    *@Param: 
    *@return: userid,list<userid>
    *@Author: 忠
    *@date: 2019/6/20
    */
    String getReceiverByMsgId(String msgId) throws Exception;
    //私有聊天文件查询
    public TableResultResponse<PrivateFileVO> getFileList(String userId, String receiverId,String query, int page, int size) throws Exception ;
}