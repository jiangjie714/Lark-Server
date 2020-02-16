package com.workhub.z.servicechat.dao.message;

import com.workhub.z.servicechat.entity.message.ZzMsgTabInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 消息标记信息表(ZzMsgTabInfo)表数据库访问层
 *
 * @author makejava
 * @since 2019-05-23 16:46:13
 */
public interface ZzMsgTabInfoDao extends Mapper<ZzMsgTabInfo> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMsgTabInfo queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMsgTabInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzMsgTabInfo 实例对象
     * @return 对象列表
     */
    List<ZzMsgTabInfo> queryAll(ZzMsgTabInfo zzMsgTabInfo);

    /**
     * 新增数据
     *
     * @param zzMsgTabInfo 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzMsgTabInfo zzMsgTabInfo);

    /**
     * 修改数据
     *
     * @param zzMsgTabInfo 实例对象
     * @return 影响行数
     */
    int update(ZzMsgTabInfo zzMsgTabInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}