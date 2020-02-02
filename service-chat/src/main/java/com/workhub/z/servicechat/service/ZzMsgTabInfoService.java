package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.entity.ZzMsgTabInfo;
import java.util.List;

/**
 * 消息标记信息表(ZzMsgTabInfo)表服务接口
 *
 * @author makejava
 * @since 2019-05-23 16:46:13
 */
public interface ZzMsgTabInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMsgTabInfo queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMsgTabInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzMsgTabInfo 实例对象
     * @return 实例对象
     */
    void insert(ZzMsgTabInfo zzMsgTabInfo);

    /**
     * 修改数据
     *
     * @param zzMsgTabInfo 实例对象
     * @return 实例对象
     */
    ZzMsgTabInfo update(ZzMsgTabInfo zzMsgTabInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}