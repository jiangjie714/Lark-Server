package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.entity.ZzMsgTabRelation;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 标记消息关系表(ZzMsgTabRelation)表数据库访问层
 *
 * @author makejava
 * @since 2019-05-23 16:12:40
 */
public interface ZzMsgTabRelationDao extends Mapper<ZzMsgTabRelation> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMsgTabRelation queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMsgTabRelation> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzMsgTabRelation 实例对象
     * @return 对象列表
     */
    List<ZzMsgTabRelation> queryAll(ZzMsgTabRelation zzMsgTabRelation);

    /**
     * 新增数据
     *
     * @param zzMsgTabRelation 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzMsgTabRelation zzMsgTabRelation);

    /**
     * 修改数据
     *
     * @param zzMsgTabRelation 实例对象
     * @return 影响行数
     */
    int update(ZzMsgTabRelation zzMsgTabRelation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    int deleteByTabId(@Param("tabId") String tabId);
}