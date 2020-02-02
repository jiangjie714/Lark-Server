package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzAt;

import java.util.List;

/**
 * 提及（@）功能实现(ZzAt)表服务接口
 *
 * @author 忠
 * @since 2019-05-10 14:22:34
 */
public interface ZzAtService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzAt queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzAt> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzAt 实例对象
     * @return 实例对象
     */
    void insert(ZzAt zzAt);

    /**
     * 修改数据
     *
     * @param zzAt 实例对象
     * @return 实例对象
     */
    Integer update(ZzAt zzAt);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);
    /**
     * 查询分页
     * @param
     * @return  分页列表
     * @author zhuqz
     * @since 2019-06-14
     */
    public TableResultResponse<ZzAt> getList(String receiverId, String groupId, int pageNum, int pageSize) throws Exception;
}