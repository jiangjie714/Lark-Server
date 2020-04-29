package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzMsgTabRelationDao;
import com.workhub.z.servicechat.entity.message.ZzMsgTabRelation;
import com.workhub.z.servicechat.service.ZzMsgTabRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.workhub.z.servicechat.config.Common.putEntityNullToEmptyString;

/**
 * 标记消息关系表(ZzMsgTabRelation)表服务实现类
 *
 * @author makejava
 * @since 2019-05-23 16:12:40
 */
@Service("zzMsgTabRelationService")
public class ZzMsgTabRelationServiceImpl implements ZzMsgTabRelationService {
    private static Logger log = LoggerFactory.getLogger(ZzMsgTabRelationServiceImpl.class);
    @Resource
    private ZzMsgTabRelationDao zzMsgTabRelationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZzMsgTabRelation queryById(String id) {
        ZzMsgTabRelation zzMsgTabRelation=this.zzMsgTabRelationDao.queryById(id);
        try {
            Common.putVoNullStringToEmptyString(zzMsgTabRelation);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        return zzMsgTabRelation;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzMsgTabRelation> queryAllByLimit(int offset, int limit) {
        return this.zzMsgTabRelationDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zzMsgTabRelation 实例对象
     * @return 实例对象
     */
    @Override
    public void insert(ZzMsgTabRelation zzMsgTabRelation) {
        try {
            putEntityNullToEmptyString(zzMsgTabRelation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int insert = this.zzMsgTabRelationDao.insert(zzMsgTabRelation);
//        return insert;
    }

    /*@Override
    protected String getPageName() {
        return null;
    }*/

    /**
     * 修改数据
     *
     * @param zzMsgTabRelation 实例对象
     * @return 实例对象
     */
    @Override
    public ZzMsgTabRelation update(ZzMsgTabRelation zzMsgTabRelation) {
        this.zzMsgTabRelationDao.update(zzMsgTabRelation);
        return this.queryById(zzMsgTabRelation.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.zzMsgTabRelationDao.deleteById(id) > 0;
    }

    @Override
    public boolean deleteByTabId(String tabId) {
        return this.zzMsgTabRelationDao.deleteByTabId(tabId) > 0;
    }
}