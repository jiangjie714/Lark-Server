package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.GroupApplyVo;
import com.workhub.z.servicechat.VO.GroupApproveVo;
import com.workhub.z.servicechat.VO.MeetingVo;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzGroupApproveDao;
import com.workhub.z.servicechat.entity.UserInfo;
import com.workhub.z.servicechat.entity.ZzGroupApprove;
import com.workhub.z.servicechat.entity.ZzGroupApproveLog;
import com.workhub.z.servicechat.feign.IUserService;
import com.workhub.z.servicechat.processor.ProcessEditGroup;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.ZzGroupApproveLogService;
import com.workhub.z.servicechat.service.ZzGroupApproveService;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzMeetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

import static com.workhub.z.servicechat.config.MessageType.MEETING_ADD;

/**
 * author:zhuqz
 * description:群审批（会议或者其他资源审批）
 * date:2019/9/11 16:14
 **/
@Service("zzGroupApproveService")
public class ZzGroupApproveServiceImpl implements ZzGroupApproveService {
    private static Logger log = LoggerFactory.getLogger(ZzGroupApproveServiceImpl.class);
    @Resource
    private ZzGroupApproveDao zzGroupApproveDao;
    @Autowired
    private ProcessEditGroup processEditGroup;
    @Autowired
    private IUserService iUserService;
    @Resource
    private ZzGroupApproveLogService zzGroupApproveLogService;
    @Resource
    private RabbitMqMsgProducer rabbitMqMsgProducer;
    @Resource
    private ZzMeetingService zzMeetingService;
    @Resource
    private ZzGroupService zzGroupService;
    //新增群信息
    @Override
    public int add(ZzGroupApprove zzGroupApprove){
        int i = this.zzGroupApproveDao.add(zzGroupApprove);
        return i;
    }
    //审批
    //返回0 已经审批
    @Transactional
    @Override
    public Map<String,String> approve(Map params){
        ZzGroupApproveLog approveLog = new ZzGroupApproveLog();
        Map p2 = new HashMap<>(16);
        p2.put("userid",params.get("userId").toString());
        UserInfo userInfo0 = iUserService.getUserInfo(p2);
        params.put("userNo",common.nulToEmptyString(userInfo0.getPId()));
        //是否已经审批了
        String ifApproveFlg = this.zzGroupApproveDao.ifApprove(params);
        Map<String,String> res = new HashMap();
        String approveFlg = params.get("approveFlg").toString();
        res.put("res","1");
        res.put("approveRes",("1".equals(approveFlg))?"通过":"不通过");
        if("0".equals(ifApproveFlg)){
           int i =  this.zzGroupApproveDao.approve(params);
        }else {
            res.put("res","0");
        }

        Map<String,Object> singleInf = this.zzGroupApproveDao.getSingleInf(params.get("id").toString());
        Date createTime = (Date) singleInf.get("CREATETIME");
        Clob clob = (Clob)singleInf.get("MSG");
        String msg = "";
        JSONArray userListuserList = null;
        try {
            msg = common.ClobToString(clob);
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("data");
            JSONObject groupJson = JSONObject.parseObject(message);
            String type = "";//类型：或者0代表群，1会议
            if((MessageType.CREATE_MEETING+"").equals(code)){
                //会议
                type = MessageType.FLOW_LOG_MEET;
            }
            if((MessageType.GROUP_CREATE+"").equals(code)){
                //群
                type = MessageType.FLOW_LOG_GROUP;
            }
            //如果是审批通过，且审批结果正常
            if("1".equals(approveFlg)&&res.get("res").equals("1")){
                //如果是群
                if(MessageType.FLOW_LOG_GROUP.equals(type)){
                    ObjectRestResponse restResponse = zzGroupService.createGroup(message);
                    //如果建群正常，发送socket
                    if(restResponse.isRel()){
                        SocketMsgVo msgVo = new SocketMsgVo();
                        msgVo.setCode(code);
                        msgVo.setReceiver(userInfo0.getId());
                        msgVo.setMsg(msg);
                        rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
                    }else{
                        res.put("res","0");
                        /*//手动回滚事务*/
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                    //如果是会议
                }else if(MessageType.FLOW_LOG_MEET.equals(type)){
                    //创建会议数据
                    zzMeetingService.createMeeting(message);
                    //推送前端
                    MeetingVo meeting = zzMeetingService.queryById(groupJson.getString("groupId"));
                    userListuserList = JSONObject.parseArray(groupJson.getString("userList"));
                    AnswerToFrontReponse answerToFrontReponse = new AnswerToFrontReponse();
                    answerToFrontReponse.setData(meeting);
                    answerToFrontReponse.setCode(MEETING_ADD);
                    String meet =  JSONObject.toJSONString(answerToFrontReponse);
                    for (int i = 0; i < userListuserList.size(); i++) {
                        String user = userListuserList.getJSONObject(i).getString("userId");
                        SocketMsgVo msgVo = new SocketMsgVo();
                        msgVo.setCode(code);
                        msgVo.setReceiver(user);
                        msgVo.setMsg(meet);
                        rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);
                        //redis 缓存处理 把用户的群列表缓存更新
                        String key = CacheConst.userMeetIds+":"+user;
                        boolean keyExist = RedisUtil.isKeyExist(key);
                        //如果key存在更新缓存，把最新的数据加入进去
                        if(keyExist){
                            RedisListUtil.putSingleWithoutDup(key,meeting.getId());
                        }

                    }

                }

            }
            if(type==null || type.equals("")){
                //默认是群
                type = MessageType.FLOW_LOG_GROUP;
            }
            //记录审批日志
            approveLog.setId(RandomId.getUUID());
            approveLog.setApprove("");
            approveLog.setApproveName("");
            approveLog.setIp(common.nulToEmptyString(params.get("ip")));
            approveLog.setApproveRes(common.nulToEmptyString(params.get("approveFlg")));
            approveLog.setGroupId(common.nulToEmptyString(groupJson.getString("groupId")));
            approveLog.setGroupName(common.nulToEmptyString(groupJson.getString("groupName")));
            approveLog.setGroupDes(common.nulToEmptyString(groupJson.getString("groupDescribe")));
            approveLog.setGroupLevel(common.nulToEmptyString(groupJson.getString("levels")));
            approveLog.setGroupPro(common.nulToEmptyString(groupJson.getString("pname")));
            approveLog.setGroupScope(common.nulToEmptyString(groupJson.getString("scop")));
            approveLog.setGroupType(common.nulToEmptyString(groupJson.getString("groupType")));
            approveLog.setOperateTime(new Date());
            approveLog.setStatus("1");
            approveLog.setType(type);
            if(!"0".equals(ifApproveFlg) || !"1".equals(res.get("res"))){
                approveLog.setStatus("0");//审批失败
            }
            approveLog.setOperateType(MessageType.FLOW_APPROVE);
            approveLog.setOperator(userInfo0.getId());
            approveLog.setOperatorNo(userInfo0.getPId());
            approveLog.setOperatorName(userInfo0.getName());


            res.put("groupId",groupJson.getString("groupId"));
            res.put("groupName",groupJson.getString("groupName"));
            res.put("type",type);
        } catch (Exception e){
            log.error(common.getExceptionMessage(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(userListuserList!=null){
                for (int i = 0; i < userListuserList.size(); i++) {
                    String user = userListuserList.getJSONObject(i).getString("userId");
                    //redis 缓存处理 把用户的群列表缓存更新
                    String key = CacheConst.userMeetIds+":"+user;
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把脏数据删除
                    if(keyExist){
                        RedisUtil.remove(key);
                    }

                }
            }
        }finally {
            if(approveLog.getGroupId()!=null){
                this.rabbitMqMsgProducer.sendMsgGroupApproveLog(approveLog);
            }
        }

        return res;
    }
    //查询我的申请群列表
    @Override
    public TableResultResponse<GroupApplyVo> getApplyGroupList(Map params) throws Exception{
        List<GroupApplyVo> dataList = new ArrayList<>();
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        PageHelper.startPage(pageNum, pageSize);
        if(params.get("type")==null){
            params.put("type","0");//默认是群
        }
        //全部查询
        if("-1".equals(params.get("type"))){
            params.put("type","");
        }
        List<Map<String,Object>> inList = this.zzGroupApproveDao.getApplyGroupList(params);
        PageInfo pageInfo = new PageInfo<>(inList);

        for(Map temp:inList){
            Clob msglb = (Clob)temp.get("MSG");
            String msgtemp = common.ClobToString(msglb);
            JSONObject jsonObject = JSONObject.parseObject(msgtemp);
            String message = jsonObject.getString("data");
            JSONObject groupJson = JSONObject.parseObject(message);
            GroupApplyVo groupApplyVo = new GroupApplyVo();
            groupApplyVo.setId(common.nulToEmptyString(temp.get("ID")));
            groupApplyVo.setApply(common.nulToEmptyString(groupJson.getString("creator")));
            groupApplyVo.setApplyName(common.nulToEmptyString(groupJson.getString("creatorName")));
            groupApplyVo.setApplyTime(common.nulToEmptyString(temp.get("CREATETIME")));
            String approveFlg = common.nulToEmptyString(temp.get("APPROVEFLG"));
            String type = common.nulToEmptyString(temp.get("TYPE"));
            if(approveFlg.equals("0")){//如果是未审批
                JSONArray approverJsonArray = JSONObject.parseArray(groupJson.getString("approveList"));
                String approveStr = "";
                String approveNameStr = "";
                for(int i=0;i<approverJsonArray.size();i++){
                    JSONObject userJson = JSONObject.parseObject(approverJsonArray.getString(i));
                    if(userJson==null){
                        continue;
                    }
                    approveStr+=","+userJson.getString("userId");
                    approveNameStr+=","+userJson.getString("userName");
                }
                if(!"".equals(approveStr)){
                    approveStr = approveStr.substring(1);
                    approveNameStr = approveNameStr.substring(1);
                }
                groupApplyVo.setApprove(approveStr);
                groupApplyVo.setApproveName(approveNameStr);
                groupApplyVo.setApproveTime("");
            }else{
                groupApplyVo.setApprove(common.nulToEmptyString(temp.get("APPROVE")));
                groupApplyVo.setApproveName(common.nulToEmptyString(temp.get("APPROVENAME")));
                groupApplyVo.setApproveTime(common.nulToEmptyString(temp.get("APPROVETIME")));
            }
            groupApplyVo.setApproveStatus(approveFlg);
            groupApplyVo.setGroupDescribe(common.nulToEmptyString(groupJson.getString("groupDescribe")));
            groupApplyVo.setGroupId(common.nulToEmptyString(groupJson.getString("groupId")));
            groupApplyVo.setGroupLevel(common.nulToEmptyString(groupJson.getString("levels")));
            groupApplyVo.setGroupName(common.nulToEmptyString(groupJson.getString("groupName")));
            groupApplyVo.setGroupScope(common.nulToEmptyString(groupJson.getString("scop")));
            groupApplyVo.setGroupPro(common.nulToEmptyString(groupJson.getString("pname")));
            JSONArray userListJsonArray = JSONObject.parseArray(groupJson.getString("userList"));
            groupApplyVo.setGroupMembers(userListJsonArray.size()+"");
            groupApplyVo.setType(type);
            dataList.add(groupApplyVo);

        }
        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                dataList
        );
        return  res;
    }
    //获取审批详细信息 id 数据主键
    @Override
    public String getApproveGroupDetail(String id){
        Map<String,Object> res = this.zzGroupApproveDao.getApproveGroupDetail(id);
        Clob clob = (Clob) res.get("MSG");
        String msgStr = null;
        try {
            msgStr = common.ClobToString(clob);
        } catch (SQLException e) {
            log.error(common.getExceptionMessage(e));
        } catch (IOException e) {
            log.error(common.getExceptionMessage(e));
        }
        return msgStr;
    }
    //根据群id获取详细信息
    @Override
    public  String getSingleInfByGroupId(String groupId){
        Map<String,Object> res = this.zzGroupApproveDao.getSingleInfByGroupId(groupId);
        Clob clob = (Clob) res.get("MSG");
        String msgStr = null;
        try {
            msgStr = common.ClobToString(clob);
        } catch (SQLException e) {
            log.error(common.getExceptionMessage(e));
        } catch (IOException e) {
            log.error(common.getExceptionMessage(e));
        }
        return msgStr;
    }
    //查询当前任审批列表
    @Override
    public TableResultResponse<GroupApproveVo> getApproveList(Map params) throws Exception{
        List<GroupApproveVo> dataList = new ArrayList<>();
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(common.getExceptionMessage(e));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Map> inList = this.zzGroupApproveDao.getApproveList(params);
        PageInfo pageInfo = new PageInfo<>(inList);

        String userId = common.nulToEmptyString(params.get("userId"));
        for(Map temp:inList){
            Clob msglb = (Clob)temp.get("MSG");
            String msgtemp = common.ClobToString(msglb);
            JSONObject jsonObject = JSONObject.parseObject(msgtemp);
            String message = jsonObject.getString("data");
            JSONObject groupJson = JSONObject.parseObject(message);
//            //审批人列表
//            boolean addResFlg = false;
//            JSONArray approverJsonArray = JSONObject.parseArray(groupJson.getString("approveList"));
//            if(approverJsonArray!=null){
//                for (int i = 0; i < approverJsonArray.size(); i++) {
//                    JSONObject userJson = JSONObject.parseObject(approverJsonArray.getString(i));
//                    String tempUserId = userJson.getString("userId");
//                    if(tempUserId.equals(userId)){
//                        addResFlg = true;
//                        break;
//                    }
//                }
//            }

            // if(addResFlg){
            String type = common.nulToEmptyString(temp.get("TYPE"));
            GroupApproveVo groupApproveVo = new GroupApproveVo();
            groupApproveVo.setId(temp.get("ID").toString());
            groupApproveVo.setCreateTime(temp.get("CREATEDATE").toString());
            //UserInfo userInfo = iUserService.info(groupJson.getString("creator"));
            groupApproveVo.setCreatorName(common.nulToEmptyString(groupJson.getString("creatorName")));
            groupApproveVo.setGroupDescribe(common.nulToEmptyString(groupJson.getString("groupDescribe")));
            groupApproveVo.setGroupMembers((JSONObject.parseArray(groupJson.getString("userList")).size()+""));
            groupApproveVo.setGroupName(groupJson.getString("groupName"));
            groupApproveVo.setGroupLevel(groupJson.getString("levels"));
            groupApproveVo.setGroupId(groupJson.getString("groupId"));
            groupApproveVo.setApproveStatus(temp.get("APPROVEFLG").toString());
            groupApproveVo.setType(type);
            dataList.add(groupApproveVo);
            // }

        }
        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                dataList
        );
        return  res;
    }
}
