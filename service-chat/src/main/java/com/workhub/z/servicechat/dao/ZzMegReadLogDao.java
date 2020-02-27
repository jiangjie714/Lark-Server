package com.workhub.z.servicechat.dao;

import com.github.pagehelper.Page;
import com.workhub.z.servicechat.entity.message.ZzMegReadLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (ZzMegReadLog)表数据库访问层
 *
 * @author makejava
 * @since 2019-09-12 17:15:24
 */
public interface ZzMegReadLogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMegReadLog queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMegReadLog> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzMegReadLog 实例对象
     * @return 对象列表
     */
    List<ZzMegReadLog> queryAll(ZzMegReadLog zzMegReadLog);

    /**
     * 新增数据
     *
     * @param zzMegReadLog 实例对象
     * @return 影响行数
     */
    int insert(ZzMegReadLog zzMegReadLog);

    /**
     * 修改数据
     *
     * @param zzMegReadLog 实例对象
     * @return 影响行数
     */
    int update(ZzMegReadLog zzMegReadLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    Page<ZzMegReadLog> queryAllReadLog(@Param("reviserName")String reviserName,
                                       @Param("senderName") String senderName);

}