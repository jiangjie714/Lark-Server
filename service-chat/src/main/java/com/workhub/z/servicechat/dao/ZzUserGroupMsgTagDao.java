package com.workhub.z.servicechat.dao;
/**
 * 用户标记群消息
 *
 * @author zhuqz
 * @since 2019-06-11
 */

import com.workhub.z.servicechat.entity.group.ZzUserGroupMsgTag;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface ZzUserGroupMsgTagDao  extends Mapper<ZzUserGroupMsgTag> {
    @Override
    int insert(ZzUserGroupMsgTag zzUserGroupMsgTag);
    int deleteByConditions(ZzUserGroupMsgTag zzUserGroupMsgTag);
    List<String> getInfList(@Param("userId")String userId, @Param("groupId")String groupId, @Param("tagType")String tagType) throws Exception;
    int deleteById(@Param("id")String id);
}
