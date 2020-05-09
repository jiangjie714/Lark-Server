package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.entity.message.ZzRecent;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ZzRecentDao  extends Mapper<ZzRecent> {
    int saveRecent(@Param("params") ZzRecent zzRecent);
    int updateRecent(@Param("params") ZzRecent zzRecent);
    int removeRecent (@Param("userId") String userId,@Param("contactId") String contactId);
    List<RecentVo> listRecents(@Param("userId") String userId);
}
