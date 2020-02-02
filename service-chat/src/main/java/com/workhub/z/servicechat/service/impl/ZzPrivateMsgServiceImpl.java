package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.PrivateFileVO;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.dao.ZzPrivateMsgDao;
import com.workhub.z.servicechat.entity.ZzPrivateMsg;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.workhub.z.servicechat.config.common.putEntityNullToEmptyString;

/**
 * 私人消息(ZzPrivateMsg)表服务实现类
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
@Service("zzPrivateMsgService")
public class ZzPrivateMsgServiceImpl implements ZzPrivateMsgService {
    private static Logger log = LoggerFactory.getLogger(ZzPrivateMsgServiceImpl.class);
    @Resource
    private ZzPrivateMsgDao zzPrivateMsgDao;

    /**
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    @Override
    public ZzPrivateMsg queryById(String msgId) {
        ZzPrivateMsg zzPrivateMsg=this.zzPrivateMsgDao.queryById(msgId);
        try {
            common.putVoNullStringToEmptyString(zzPrivateMsg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        return zzPrivateMsg;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzPrivateMsg> queryAllByLimit(int offset, int limit) {
        return this.zzPrivateMsgDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zzPrivateMsg 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public void insert(ZzPrivateMsg zzPrivateMsg) {
        try {
            putEntityNullToEmptyString(zzPrivateMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int insert = this.zzPrivateMsgDao.insert(zzPrivateMsg);
//        return insert;
    }

    /*@Override
    protected String getPageName() {
        return null;
    }
*/
    /**
     * 修改数据
     *
     * @param zzPrivateMsg 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public Integer update(ZzPrivateMsg zzPrivateMsg) {
        int update = this.zzPrivateMsgDao.update(zzPrivateMsg);
        return update;
    }

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteById(String msgId) {
        return this.zzPrivateMsgDao.deleteById(msgId) > 0;
    }
    /**
     * 查询消息记录
     * @auther zhuqz
     * @param param 参数集合：sender发送人，receiver接收人，begin_time开始时间，end_time结束时间
     * @return 对象列表
     */
    @Override
    public TableResultResponse<ZzPrivateMsg> queryMsg(Map<String,String> param) throws Exception{
        List<ZzPrivateMsg> dataList = null;
        String begin_time = param.get("begin_time");//查询开始时间
        if(begin_time==null || "".equals(begin_time)){
            begin_time="2019-01-01";//时间的无穷小
        }
        String end_time = param.get("end_time");//查询结束时间
        if(end_time==null || "".equals(end_time)){
            end_time="2999-12-31";//时间的无穷大
        }


        //目前数据库消息备份是每个月一号凌晨备份上上个月的记录，同时删除当前消息记录
        //例如当前日期是2019-06-01 那么定时任务把2019-05-01之前的消息记录备份到历史表，同时删除当前表的记录
        //所有如果以后定时任务的逻辑有变化，这里查询逻辑也要做修改
        int pageNum = Integer.valueOf(param.get("page"));
        int pageSize = Integer.valueOf(param.get("size"));
        PageHelper.startPage(pageNum, pageSize);

        String lastMonthFirstDay= common.getBeforeMonthFirstDay();//上个月第一天
        if(begin_time.compareTo(lastMonthFirstDay)>=0){//如果当前查询开始时间大于上个月1号，当期表
            dataList=this.zzPrivateMsgDao.queryMsgRecent(param);
        }else if(end_time.compareTo(lastMonthFirstDay)<0){//如果当前查询结束时间小于上个月1号，只查询历史表
            dataList=this.zzPrivateMsgDao.queryMsgHis(param);
        }else{//询历史表+最近表
            dataList=this.zzPrivateMsgDao.queryMsgCurrentAndHis(param);
        }
        common.putVoNullStringToEmptyString(dataList);
        PageInfo<ZzPrivateMsg> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<ZzPrivateMsg> res = new TableResultResponse<ZzPrivateMsg>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return  res;
    }
    /**
     *@Description: 根据消息ID查询发送人详细信息
     *@Param:
     *@return: userid
     *@Author: 忠
     *@date: 2019/6/20
     */
    @Override
    public  String getSenderByMsgId(String msgId) throws Exception{
        ZzPrivateMsg zzPrivateMsg = this.zzPrivateMsgDao.queryById(msgId);
        return zzPrivateMsg.getMsgSender();
    }

    /**
     *@Description: 根据消息ID查询接收人详细信息（若为群组则返回当前群组内userList）
     *@Param:
     *@return: userid,list<userid>
     *@Author: 忠
     *@date: 2019/6/20
     */
    @Override
    public String getReceiverByMsgId(String msgId) throws Exception{
        ZzPrivateMsg zzPrivateMsg = this.zzPrivateMsgDao.queryById(msgId);
        return zzPrivateMsg.getMsgReceiver();
    }
    /**
     * 私有聊天文件信息
     * @param
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @Override
    //query 查询附件名称
    public TableResultResponse<PrivateFileVO> getFileList(String userId,String receiverId,String query, int page, int size) throws Exception {
        PageHelper.startPage(page, size);
        List<PrivateFileVO> dataList =this.zzPrivateMsgDao.getFileList(userId,receiverId,query);
        //null的String类型属性转换空字符串
        common.putVoNullStringToEmptyString(dataList);
        PageInfo<PrivateFileVO> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<PrivateFileVO> res = new TableResultResponse<PrivateFileVO>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }
}