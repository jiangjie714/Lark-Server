package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.config.CacheConst;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.ZzUserOnlineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author:zhuqz
 * description: 用户在线状态列表获取
 * date:2019/12/10 15:24
 **/
@Service("zzUserOnlineService")
public class ZzUserOnlineServiceImpl implements ZzUserOnlineService {
    /**
     * 获取全部在线用户，返回其id列表
     * @return
     */
    @Override
    public List<String> getAllOnlineUserList() {
        List<String> onlineUserList = RedisListUtil.getList(CacheConst.USER_ONLINE_LIST);
        return onlineUserList;
    }
    @Override
    /**
     * 用户是否在线
     */
    public boolean isUserOnline(String userId) {
        String online = Common.nulToEmptyString(RedisUtil.getValue(CacheConst.userOnlineCahce+ Common.nulToEmptyString(userId)));
        return (MessageType.ONLINE+"").equals(online);
    }
}
