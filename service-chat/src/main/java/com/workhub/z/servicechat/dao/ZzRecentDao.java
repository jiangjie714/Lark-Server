package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.entity.message.ZzRecent;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ZzRecentDao  extends Mapper<ZzRecent> {
    int add(@Param("params") ZzRecent zzRecent);
    int update(@Param("params") ZzRecent zzRecent);
    int deleteData (@Param("userId") String userId,@Param("contactId") String contactId);
    List<RecentVo> getList(@Param("userId") String userId);
}
