package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.dao.ZzRecentDao;
import com.workhub.z.servicechat.entity.message.ZzRecent;
import com.workhub.z.servicechat.service.ZzRecentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/4/30 16:28
 * @description: 最近联系人
 */
@Service("zzRecentService")
public class ZzRecentServiceImpl implements ZzRecentService {
    @Autowired
    ZzRecentDao zzRecentDao;

    /**
     * 添加最近联系人
     * @param zzRecent
     * @return
     */
    @Override
    public int add(ZzRecent zzRecent) {
        zzRecent.setId(RandomId.getUUID());
        return this.zzRecentDao.add(zzRecent);
    }

    /**
     * 更新数据
     * @param zzRecent
     * @return
     */
    @Override
    public int update(ZzRecent zzRecent) {
        return this.zzRecentDao.update(zzRecent);
    }

    /**
     * 删除消息
     * @param userId
     * @param contactId
     * @return
     */
    @Override
    public int delete(String userId, String contactId) {
        return this.zzRecentDao.deleteData(userId,contactId);
    }

    /**
     * 获取最近联系人列表
     * @param userId
     * @return
     */
    @Override
    public List<RecentVo> getList(String userId) {
        return this.zzRecentDao.getList(userId);
    }
}