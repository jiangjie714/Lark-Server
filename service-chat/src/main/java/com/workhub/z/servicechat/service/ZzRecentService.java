package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.VO.NoReadVo;
import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.entity.message.ZzRecent;

import java.util.List;

public interface ZzRecentService {
    /**
     * 保存
     * @param zzRecent
     * @return
     */
    int saveRecent(ZzRecent zzRecent);

    /**
     * 修改
     * @param zzRecent
     * @return
     */
    int updateRecent(ZzRecent zzRecent);

    /**
     * 删除
     * @param userId
     * @param contactId
     * @return
     */
    int removeRecent(String userId,String contactId);

    /**
     * 最近联系人列表
     * @param userId
     * @return
     */
    List<RecentVo> listRecents(String userId);

    /**
     * 查询单条
     * @param userId
     * @param contactId
     * @return
     */
    ZzRecent getRecentData(String userId,String contactId);
    /**
     * 查询当前人未读消息
     * @param userId
     * @return
     */
    List<NoReadVo> listNoReadMsgsByUserId(String userId);
    /**
     * 查询未读消息条数
     * @param userId
     * @param contactId
     * @return
     */
    int getNoReadMsgNum(String userId, String contactId);
}
