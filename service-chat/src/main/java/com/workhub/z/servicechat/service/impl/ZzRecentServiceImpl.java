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
    public int saveRecent(ZzRecent zzRecent) {
        zzRecent.setId(RandomId.getUUID());
        return this.zzRecentDao.saveRecent(zzRecent);
    }

    /**
     * 更新数据
     * @param zzRecent
     * @return
     */
    @Override
    public int updateRecent(ZzRecent zzRecent) {
        return this.zzRecentDao.updateRecent(zzRecent);
    }

    /**
     * 删除消息
     * @param userId
     * @param contactId
     * @return
     */
    @Override
    public int removeRecent(String userId, String contactId) {
        return this.zzRecentDao.removeRecent(userId,contactId);
    }

    /**
     * 获取最近联系人列表
     * @param userId
     * @return
     */
    @Override
    public List<RecentVo> listRecents(String userId) {
        return this.zzRecentDao.listRecents(userId);
    }

    /**
     * 查询单条
     * @param userId
     * @param contactId
     * @return
     */
    @Override
    public ZzRecent getRecentData(String userId, String contactId) {
        return this.zzRecentDao.getRecentData(userId,contactId);
    }
}