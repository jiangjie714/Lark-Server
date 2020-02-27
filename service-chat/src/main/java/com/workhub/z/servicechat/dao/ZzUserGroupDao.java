package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.GroupListVo;
import com.workhub.z.servicechat.VO.UserNewMsgVo;
import com.workhub.z.servicechat.entity.group.ZzUserGroup;
import com.workhub.z.servicechat.model.RawMessageDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 用户群组映射表(ZzUserGroup)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-10 14:22:54
 */
public interface ZzUserGroupDao extends Mapper<ZzUserGroup> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzUserGroup queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzUserGroup> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzUserGroup 实例对象
     * @return 对象列表
     */
    List<ZzUserGroup> queryAll(ZzUserGroup zzUserGroup);

    /**
     * 新增数据
     *
     * @param zzUserGroup 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzUserGroup zzUserGroup);
    //批量新增
    int addMemeberList(@Param("groupId")String groupId,@Param("userId")String userId,@Param("members") List<ZzUserGroup> members);

    /**
     * 修改数据
     *
     * @param zzUserGroup 实例对象
     * @return 影响行数
     */
    int update(ZzUserGroup zzUserGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    List<GroupListVo> groupList(@Param("id")String id, @Param("start")Integer start, @Param("end")Integer end);
    //以前的分页查询有问题，这里重载一个方法
    List<GroupListVo> groupList(@Param("id")String id);

    Long groupListTotal(@Param("id")String id);

    Long deleteByGroupIdAndUserId(@Param("groupId") String groupId,@Param("userId") String userId);
    //批量删除
    Long deleteByGroupIdAndUserIdList(@Param("groupId") String groupId,@Param("members") List<String> members);

    int deleteByGroupId(@Param("groupId") String groupId);

    List<UserNewMsgVo> getUserNewMsgList2(@Param("id")String id);
    List<RawMessageDto> getUserNewMsgList(@Param("id")String id);
    /**
     * 修改用户群个性化信息--是否置顶
     * @param userId 用户id；groupId 群id；topFlg 1置顶，0不置顶
     * @return  受影响行数
     * @author zhuqz
     * @since 2019-06-11
     */
    int setUserGroupTop(@Param("userId") String userId, @Param("groupId") String groupId, @Param("topFlg") String topFlg);
    /**
     * 修改用户群个性化信息--是否置顶
     * @param userId 用户id；groupId 群id；muteFlg 1免打扰，0否
     * @return  受影响行数
     * @author zhuqz
     * @since 2019-06-11
     */
    int setUserGroupMute(@Param("userId") String userId, @Param("groupId") String groupId, @Param("muteFlg") String topFlg);

    //获取一个群组有多少成员
    int getGroupUserCount(@Param("groupId") String groupId);
    //获取群前九个人的头像地址
    List<String> getGroupUserHeadList(@Param("groupId") String groupId);

    Long queryInGroup(@Param("userId") String userId,@Param("groupId") String gourpId);

    /**
     * 根据用户id查询用户的群列表
     * @param userId
     * @return
     */
    List<String> getGroupByUserId(@Param("userId") String userId);
}