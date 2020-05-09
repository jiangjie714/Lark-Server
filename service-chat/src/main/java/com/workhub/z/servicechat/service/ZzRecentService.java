package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.entity.message.ZzRecent;

import java.util.List;

public interface ZzRecentService {
    int saveRecent(ZzRecent zzRecent);
    int updateRecent(ZzRecent zzRecent);
    int removeRecent(String userId,String contactId);
    List<RecentVo> listRecents(String userId);
}
