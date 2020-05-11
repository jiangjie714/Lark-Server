package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.entity.message.ZzRecent;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ZzRecentDao  extends Mapper<ZzRecent> {
    /**
     * 保存
     * @param zzRecent
     * @return
     */
    int saveRecent(@Param("params") ZzRecent zzRecent);

    /**
     * 修改
     * @param zzRecent
     * @return
     */
    int updateRecent(@Param("params") ZzRecent zzRecent);

    /**
     * 删除
     * @param userId
     * @param contactId
     * @return
     */
    int removeRecent (@Param("userId") String userId,@Param("contactId") String contactId);

    /**
     * 查询最近联系人列表
     * @param userId
     * @return
     */
    List<RecentVo> listRecents(@Param("userId") String userId);

    /**
     * 查询最近联系人单条
     * @param userId
     * @param contactId
     * @return
     */
    ZzRecent getRecentData(@Param("userId") String userId,@Param("contactId") String contactId);

}
