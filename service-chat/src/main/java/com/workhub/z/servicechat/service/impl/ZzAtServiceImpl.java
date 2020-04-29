package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzAtDao;
import com.workhub.z.servicechat.entity.message.ZzAt;
import com.workhub.z.servicechat.service.ZzAtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.workhub.z.servicechat.config.Common.putEntityNullToEmptyString;

/**
 * 提及（@）功能实现(ZzAt)表服务实现类
 *
 * @author 忠
 * @since 2019-05-10 14:22:34
 */
@Service("zzAtService")
public class ZzAtServiceImpl implements ZzAtService  {
    private static Logger log = LoggerFactory.getLogger(ZzAtServiceImpl.class);
    @Resource
    private ZzAtDao zzAtDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZzAt queryById(String id) {
        ZzAt zzAt = this.zzAtDao.queryById(id);
        try {
            Common.putVoNullStringToEmptyString(zzAt);
        } catch (Exception e) {
            log.error(Common.getExceptionMessage(e));

        }
        return zzAt;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
//    @Override
//    public List<ZzAt> queryAllByLimit(int offset, int limit) {
//        return this.zzAtDao.queryAllByLimit(offset, limit);
//    }

    /**
     * 新增数据
     *
     * @param zzAt 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public void insert(ZzAt zzAt) {
        //super.insert(zzAt);
        try {
            putEntityNullToEmptyString(zzAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int insert = this.zzAtDao.insert(zzAt);
//        return insert;
    }

    /**
     * 修改数据
     *
     * @param zzAt 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public Integer update(ZzAt zzAt) {
        int update = this.zzAtDao.update(zzAt);
        return update;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteById(String id) {
        return this.zzAtDao.deleteById(id) > 0;
    }

    /*@Override
    protected String getPageName() {
        return null;
    }*/
    /**
     * 查询分页
     * @param
     * @return  分页列表
     * @author zhuqz
     * @since 2019-06-14
     */
    @Override
    public TableResultResponse<ZzAt> getList(String receiverId, String groupId, int pageNum, int pageSize) throws Exception{
        PageHelper.startPage(pageNum, pageSize);
        List<ZzAt> list = this.zzAtDao.getList(receiverId,groupId);
        try {
            Common.putVoNullStringToEmptyString(list);
        } catch (Exception e) {
            log.error(Common.getExceptionMessage(e));

        }
        PageInfo<ZzAt> pageInfo = new PageInfo<>(list);
        TableResultResponse<ZzAt> res = new TableResultResponse<ZzAt>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }
    /**
     * at已读消息删除
     * @param msgId 消息id
     * @param userId 人员id
     * @return
     */
    @Override
    public int deleteByMsgIdAndUserId(String msgId,String userId){
        return this.zzAtDao.deleteByMsgIdAndUserId(msgId,userId);
    }
}