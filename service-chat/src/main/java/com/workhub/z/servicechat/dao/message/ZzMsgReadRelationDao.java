package com.workhub.z.servicechat.dao.message;

import com.workhub.z.servicechat.VO.NoReadVo;
import com.workhub.z.servicechat.entity.message.ZzMsgReadRelation;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 消息阅读状态关系表(ZzMsgReadRelation)表数据库访问层
 *
 * @author makejava
 * @since 2019-05-23 13:27:22
 */
public interface ZzMsgReadRelationDao extends Mapper<ZzMsgReadRelation> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMsgReadRelation queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMsgReadRelation> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzMsgReadRelation 实例对象
     * @return 对象列表
     */
    List<ZzMsgReadRelation> queryAll(ZzMsgReadRelation zzMsgReadRelation);

    /**
     * 新增数据
     *
     * @param zzMsgReadRelation 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzMsgReadRelation zzMsgReadRelation);

    /**
     * 修改数据
     *
     * @param zzMsgReadRelation 实例对象
     * @return 影响行数
     */
    int update(ZzMsgReadRelation zzMsgReadRelation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    boolean deleteByConsumerAndSender(@Param("sender") String sender, @Param("consumer") String consumer);

    Long queryNoReadCount(@Param("consumer")String consumer);

    List<NoReadVo> queryNoReadCountList(@Param("consumer")String consumer);

    Long queryNoReadMsgBySenderAndReceiver(@Param("sender") String sender, @Param("receiver") String receiver);
}