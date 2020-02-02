package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.VO.NoReadVo;
import com.workhub.z.servicechat.entity.ZzMsgReadRelation;
import java.util.List;

/**
 * 消息阅读状态关系表(ZzMsgReadRelation)表服务接口
 *
 * @author makejava
 * @since 2019-05-23 13:27:22
 */
public interface ZzMsgReadRelationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMsgReadRelation queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMsgReadRelation> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzMsgReadRelation 实例对象
     * @return 实例对象
     */
    void insert(ZzMsgReadRelation zzMsgReadRelation);

    /**
     * 修改数据
     *
     * @param zzMsgReadRelation 实例对象
     * @return 实例对象
     */
    ZzMsgReadRelation update(ZzMsgReadRelation zzMsgReadRelation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    //删除已读关系记录
    boolean deleteByConsumerAndSender(String sender, String consumer);
    //获取未读消息数
    Long queryNoReadCount(String consumer);
    //获取未读消息列表
    List<NoReadVo> queryNoReadCountList(String consumer);
    /**
    *@Description: 获取未读消息条数
    *@Param: 
    *@return: 
    *@Author: 忠
    *@date: 2019/6/12
    */
    int queryNoReadMsgBySenderAndReceiver(String sender, String receiver);
}