package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.GroupUserListVo;
import com.workhub.z.servicechat.VO.GroupVo;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 群组表(ZzGroup)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-10 14:29:32
 */
public interface ZzGroupDao extends Mapper<ZzGroup> {

    /**
     * 通过ID查询单条数据
     *
     * @param groupId 主键
     * @return 实例对象
     */
    ZzGroup queryById(String groupId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzGroup> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzGroup 实例对象
     * @return 对象列表
     */
    List<ZzGroup> queryAll(ZzGroup zzGroup);

    /**
     * 新增数据
     *
     * @param zzGroup 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzGroup zzGroup);

    void addGroup(ZzGroup zzGroup);

    /**
     * 修改数据
     *
     * @param zzGroup 实例对象
     * @return 影响行数
     */
    int update(ZzGroup zzGroup);

    /**
     * 通过主键删除数据
     *
     * @param groupId 主键
     * @return 影响行数
     */
    int deleteById(String groupId);

    List<GroupUserListVo> groupUserList(@Param("id")String id,@Param("start")Integer start,@Param("end")Integer end);

    Long groupUserListTotal(@Param("groupId")String groupId);

    List<ZzGroup> queryGroupListByUserId(@Param("id") String id);


    List<String> queryGroupUserIdListByGroupId(@Param("groupId")String groupId);
    /**
     * 逻辑删除群
     * @param groupId 群id
     * @return  受影响行数
     * @author zhuqz
     * @since 2019-06-11
     */
    int deleteGroupLogic(@Param("groupId") String groupId, @Param("delFlg") String delFlg,@Param("userId")String userId);

    /**
     * 群组信息监控
     * param:group_name群组名称；creator创建人姓名；level密级；
     * dateBegin创建时间开始；dateEnd创建时间结束；pname项目名称；isclose是否关闭
     */
    List<GroupVo> groupListMonitoring(@Param("params") Map<String,String> param);
}