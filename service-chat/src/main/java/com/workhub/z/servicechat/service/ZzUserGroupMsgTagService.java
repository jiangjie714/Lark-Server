package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzUserGroupMsgTag;

/**
 * 用户标记群消息
 *
 * @author zhuqz
 * @since 2019-06-11
 */
public interface ZzUserGroupMsgTagService {
    /**
     * 增加群消息标记
     * @param entity 数据库实体
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    String addUserGroupMsgTag(ZzUserGroupMsgTag entity) throws Exception;
    /**
     * 按照id删除群消息标记
     * @param id 数据库id
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    public String deleteById(String id) throws Exception;
    /**
     * 按照条件（用户id，群id，消息id，标记类型）删除群消息标记
     * @param entity 数据库实体
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    public String deleteByConditions(ZzUserGroupMsgTag entity) throws Exception;

    /**
     * 查询用户标记消息（自己实现分页，目前父类分页不支持排序、查询条件复杂化）
     * @param
     * @return  分页列表
     * @author zhuqz
     * @since 2019-06-11
     */
    public TableResultResponse getUserGroupMsgTagList(String userId,String groupId,String tagType,int pageNum,int pageSize) throws Exception;
}
