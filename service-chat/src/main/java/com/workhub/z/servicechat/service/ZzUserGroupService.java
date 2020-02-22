package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.vo.rpcvo.ContactVO;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.GroupListVo;
import com.workhub.z.servicechat.VO.UserNewMsgVo;
import com.workhub.z.servicechat.entity.group.ZzUserGroup;
import com.workhub.z.servicechat.model.RawMessageDto;

import java.util.List;

/**
 * 用户群组映射表(ZzUserGroup)表服务接口
 *
 * @author 忠
 * @since 2019-05-10 14:22:54
 */
public interface ZzUserGroupService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzUserGroup queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzUserGroup> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzUserGroup 实例对象
     * @return 实例对象
     */
    void insert(ZzUserGroup zzUserGroup);

    /**
     * 修改数据
     *
     * @param zzUserGroup 实例对象
     * @return 实例对象
     */
    Integer update(ZzUserGroup zzUserGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    //获取群组信息分页
    PageInfo<GroupListVo> groupUserList(String id, int page, int size) throws Exception;
    //总记录数
    Long groupUserListTotal(String id) throws Exception;
    //获取最新消息列表
    List<UserNewMsgVo> getUserNewMsgList2(String id);
    List<RawMessageDto> getUserNewMsgList(String id);
    //获取最新联系人列表
    List<ContactVO> getContactVOList2(String id);
    List<ContactVO> getContactVOList(String id);
    /**
     * 修改用户群个性化信息--是否置顶
     * @param userId 用户id；groupId 群id；topFlg 1置顶，0不置顶
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    String setUserGroupTop(String userId,String gourpId,String topFlg) throws Exception;
    /**
     * 修改用户群个性化信息--是否免打扰
     * @param userId 用户id；groupId 群id；muteFlg 1免打扰，0否
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    String setUserGroupMute(String userId,String gourpId,String topFlg) throws Exception;

    //获取群里有多少成员
    int getGroupUserCount(String groupid)throws Exception;
    //获取群前九个人的头像地址
    List<String> getGroupUserHeadList(String groupid)throws Exception;

    Long queryInGroup(String userId,String gourpId)throws Exception;

    /**
     * 获取用户所有群id的集合
     * @param userId
     * @return
     */
    List<String> getGroupByUserId(String userId);
}