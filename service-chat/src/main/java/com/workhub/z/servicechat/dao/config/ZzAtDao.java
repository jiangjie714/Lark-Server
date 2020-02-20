package com.workhub.z.servicechat.dao.config;

import com.workhub.z.servicechat.entity.message.ZzAt;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 提及（@）功能实现(ZzAt)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-10 14:22:30
 */
public interface ZzAtDao extends Mapper<ZzAt> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzAt queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzAt> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzAt 实例对象
     * @return 对象列表
     */
    List<ZzAt> queryAll(ZzAt zzAt);

    /**
     * 新增数据
     *
     * @param zzAt 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzAt zzAt);

    /**
     * 修改数据
     *
     * @param zzAt 实例对象
     * @return 影响行数
     */
    int update(ZzAt zzAt);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);
    /**
     * 查询分页
     * @param
     * @return  分页列表
     * @author zhuqz
     * @since 2019-06-14
     */
    List<ZzAt> getList(@Param("receiverId")String receiverId, @Param("groupId")String groupId) throws Exception;

    /**
     * 删除AT已读信息
     * @param msgId 消息id
     * @param userId 用户id
     * @return
     */
    int deleteByMsgIdAndUserId(@Param("msgId") String msgId,@Param("userId") String userId);

}