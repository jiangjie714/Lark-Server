package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.workhub.z.servicechat.VO.MegReadLogVO;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.dao.ZzGroupDao;
import com.workhub.z.servicechat.entity.UserInfo;
import com.workhub.z.servicechat.entity.ZzGroup;
import com.workhub.z.servicechat.entity.ZzMegReadLog;
import com.workhub.z.servicechat.dao.ZzMegReadLogDao;
import com.workhub.z.servicechat.service.IUserService;
import com.workhub.z.servicechat.service.ZzMegReadLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (ZzMegReadLog)表服务实现类
 *
 * @author makejava
 * @since 2019-09-12 17:15:24
 */
@Service("zzMegReadLogService")
public class ZzMegReadLogServiceImpl implements ZzMegReadLogService {
    private static Logger log = LoggerFactory.getLogger(ZzMessageInfoServiceImpl.class);
    @Autowired
    private IUserService userService;
    @Resource
    private ZzMegReadLogDao zzMegReadLogDao;
    @Resource
    private ZzGroupDao groupDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZzMegReadLog queryById(String id) {
        return this.zzMegReadLogDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzMegReadLog> queryAllByLimit(int offset, int limit) {
        return this.zzMegReadLogDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zzMegReadLog 实例对象
     * @return 实例对象
     */
    @Override
    public ZzMegReadLog insert(ZzMegReadLog zzMegReadLog) {
        try {
            common.putVoNullStringToEmptyString(zzMegReadLog);
        }catch (Exception e){
            log.error(common.getExceptionMessage(e));
        }
        this.zzMegReadLogDao.insert(zzMegReadLog);
        return zzMegReadLog;
    }

    /**
     * 修改数据
     *
     * @param zzMegReadLog 实例对象
     * @return 实例对象
     */
    @Override
    public ZzMegReadLog update(ZzMegReadLog zzMegReadLog) {
        this.zzMegReadLogDao.update(zzMegReadLog);
        return this.queryById(zzMegReadLog.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.zzMegReadLogDao.deleteById(id) > 0;
    }
    /**
     * 获取数据
     *
     *
     * @return 是否成功
     */
    @Override
    public TableResultResponse queryAllReadLog(Integer pageNum,
                                               Integer pageSize,
                                               String reviserName,
                                               String senderName){
        PageHelper.startPage(pageNum, pageSize);
        Page<ZzMegReadLog> dataList = this.zzMegReadLogDao.queryAllReadLog(reviserName,senderName);
        List<MegReadLogVO> megReadLogList = new ArrayList<>();
        for(ZzMegReadLog data:dataList){
            MegReadLogVO megReadLogVO = new MegReadLogVO();
            megReadLogVO.setId(data.getId());
            megReadLogVO.setReadtime(data.getReadtime());
            megReadLogVO.setSender(data.getSender());
            Map p = new HashMap<>(16);
            p.put("userid",data.getSender());
            UserInfo userInfo = userService.getUserInfo(p);
            if (userInfo.getId() != null){
                megReadLogVO.setSenderName(userInfo.getName());
                megReadLogVO.setSenderSN(userInfo.getPId());
            }else{
                ZzGroup groupinfo = groupDao.queryById(data.getSender());
                megReadLogVO.setSenderName(groupinfo.getGroupName());
                megReadLogVO.setSenderSN(groupinfo.getGroupId());
            }
            Map p2 = new HashMap<>(16);
            p2.put("userid",data.getReviser());
            UserInfo userinfo1 = userService.getUserInfo(p2);
            megReadLogVO.setReviser(data.getReviser());
            megReadLogVO.setReviserName(userinfo1.getName());
            megReadLogVO.setReviserSN(userinfo1.getPId());
            megReadLogList.add(megReadLogVO);
        }
        try {
            common.putVoNullStringToEmptyString(megReadLogList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }

        TableResultResponse res = new TableResultResponse(
                dataList.getPageSize(),
                dataList.getPageNum(),
                dataList.getPages(),
                dataList.getTotal(),
                megReadLogList
        );
        return res;
    };
}