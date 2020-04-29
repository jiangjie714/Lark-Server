package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.GroupUserListVo;
import com.workhub.z.servicechat.VO.GroupVo;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.model.GroupEditDto;

import java.util.List;
import java.util.Map;

/**
 * 群组表(ZzGroup)表服务接口
 *
 * @author 忠
 * @since 2019-05-10 14:29:32
 */
public interface ZzGroupService {

    /**
     * 通过ID查询单条数据
     *
     * @param groupId 主键
     * @return 实例对象
     */
    ZzGroup queryById(String groupId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzGroup> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzGroup 实例对象
     * @return 实例对象
     */
    void insert(ZzGroup zzGroup);

    /**
     * 修改数据
     *
     * @param zzGroup 实例对象
     * @return 实例对象
     */
    Integer update(ZzGroup zzGroup);

    /**
     * 通过主键删除数据
     *
     * @param groupId 主键
     * @return 是否成功
     */
    boolean deleteById(String groupId);

    //获取群组成员信息
    PageInfo<GroupUserListVo> groupUserList(String id, int page, int size) throws Exception;
    //总记录数
    Long groupUserListTotal(String groupId) throws Exception;

   List<String> queryGroupUserIdListByGroupId(String groupId);
    /**
    *@Description: 根据用户id查询用户所在群组
    *@Param: userId
    *@return: list<zzGroup>
    *@Author: 忠
    *@date: 2019/5/29
    */
    List<ZzGroup> queryGroupListByUserId(String id) throws Exception;

    /**
     * 逻辑删除群
     * @param groupId 群id
     * @return  1成功；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    String deleteGroupLogic(String groupId, String delFlg,String userId) throws Exception;
    //获取群成员列表
    String getGroupUserList(String groupId) throws Exception;
    //群组信息监控
    //param:group_name群组名称；creator创建人姓名；level密级；
    // dateBegin创建时间开始；dateEnd创建时间结束；pname项目名称；isclose是否关闭
    TableResultResponse<GroupVo> groupListMonitoring(Map<String,String> params) throws Exception;

    /**
     * 解散研讨组
     * @author dd
     * @param groupId
     */
    void dissolveGroup(String groupId,String userId,String userName);

    /**
     * 移除研讨组成员
     * @author dd
     * @param groupId
     * @param userId
     */
    void removeMember(String groupId, String userId);

    /**
     * 添加研讨组成员
     * @author dd
     * @param groupId
     * @param userId
     */
    void addMember(String groupId, String userId);

    /**
     * 群编辑接口
     */
    int groupMemberEdit(GroupEditDto groupEditDto, String userId, String userName);

    /**
     * 创建研讨群组
     * @param groupJsonStr json字符串
     * @return
     */
    ObjectRestResponse createGroup(String groupJsonStr);
}