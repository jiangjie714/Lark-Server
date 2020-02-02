package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzMegReadLog;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * (ZzMegReadLog)表服务接口
 *
 * @author makejava
 * @since 2019-09-12 17:15:24
 */
public interface ZzMegReadLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzMegReadLog queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMegReadLog> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzMegReadLog 实例对象
     * @return 实例对象
     */
    ZzMegReadLog insert(ZzMegReadLog zzMegReadLog);

    /**
     * 修改数据
     *
     * @param zzMegReadLog 实例对象
     * @return 实例对象
     */
    ZzMegReadLog update(ZzMegReadLog zzMegReadLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);
    public TableResultResponse queryAllReadLog(Integer pageNum,
                                               Integer pageSize,
                                               String reviserName,
                                               String senderName);
}