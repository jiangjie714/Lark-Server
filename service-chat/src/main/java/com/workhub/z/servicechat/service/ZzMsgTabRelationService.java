package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.entity.ZzMsgTabRelation;
import java.util.List;

/**
 * 标记消息关系表(ZzMsgTabRelation)表服务接口
 *
 * @author makejava
 * @since 2019-05-23 16:12:40
 */
public interface ZzMsgTabRelationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMsgTabRelation queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMsgTabRelation> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzMsgTabRelation 实例对象
     * @return 实例对象
     */
    void insert(ZzMsgTabRelation zzMsgTabRelation);

    /**
     * 修改数据
     *
     * @param zzMsgTabRelation 实例对象
     * @return 实例对象
     */
    ZzMsgTabRelation update(ZzMsgTabRelation zzMsgTabRelation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    //删除记录
    boolean deleteByTabId(String tabId);
}