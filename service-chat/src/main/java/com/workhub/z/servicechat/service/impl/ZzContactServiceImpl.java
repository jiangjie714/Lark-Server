package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.dao.ZzContactDao;
import com.workhub.z.servicechat.entity.ZzContactInf;
import com.workhub.z.servicechat.service.ZzContactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:zhuqz
 * description: 联系人
 * date:2019/12/3 15:02
 **/
@Service("zzContactService")
public class ZzContactServiceImpl implements ZzContactService {
    @Resource
    ZzContactDao zzContactDao;
    @Override
    public String countsById(String id) {
        return this.zzContactDao.countsById(id);
    }

    @Override
    public int add(ZzContactInf zzContactInf) {
        return this.zzContactDao.add(zzContactInf);
    }

    @Override
    public int update(ZzContactInf zzContactInf) {
        return this.zzContactDao.update(zzContactInf);
    }

    @Override
    public int deleteData(String id) {
        return this.zzContactDao.deleteData(id);
    }
}
