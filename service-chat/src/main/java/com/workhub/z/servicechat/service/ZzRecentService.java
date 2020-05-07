package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.entity.message.ZzRecent;

import java.util.List;

public interface ZzRecentService {
    int add(ZzRecent zzRecent);
    int update(ZzRecent zzRecent);
    int delete(String userId,String contactId);
    List<RecentVo> getList(String userId);
}
