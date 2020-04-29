package com.workhub.z.servicechat.service;

import java.util.List;

/**
 * @author:zhuqz
 * description:用户在线状态列表获取
 * date:2019/12/10 15:24
 **/
public interface ZzUserOnlineService {
    /**
     * 获取全部在线用户，返回缓存列表
     * @return
     */
    List<String> getAllOnlineUserList();

    /**
     * 用户是否在线
     * @param userId
     * @return
     */
    boolean isUserOnline(String userId);
}
