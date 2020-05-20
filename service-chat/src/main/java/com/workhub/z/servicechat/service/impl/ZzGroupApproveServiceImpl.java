package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzGroupApproveDao;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.entity.group.ZzGroupApprove;
import com.workhub.z.servicechat.entity.group.ZzGroupApproveLog;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.model.GroupTaskDto;
import com.workhub.z.servicechat.model.UserListDto;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.*;
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
import java.util.stream.Collectors;

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
    private AdminUserService iUserService;
    @Resource
    private RabbitMqMsgProducer rabbitMqMsgProducer;
    @Resource
    private ZzMeetingService zzMeetingService;
    @Resource
    private ZzGroupService zzGroupService;
    @Resource
    ZzRequireApproveAuthorityService zzRequireApproveAuthorityService;
    /**
     * 添加审批
     * @param msg
     * @param userId
     * @param userName
     * @param userNo
     * @param userIp
     * @return 1成功0失败
     */
    @Override
    public int saveApprove(String msg,String userId,String userName,String userNo,String userIp){
        int res =  ResConst.INT_RES_SUCCESS;
        //群（资源）id
        String groupId = RandomId.getUUID();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("data");
        JSONObject groupJson = JSONObject.parseObject(message);
        //0群1会议
        String type = "";
        //会议
        if(MessageType.CREATE_APPROVE_MEET.equals(code)){
            type = MessageType.FLOW_LOG_MEET;
        }
        //群
        if(MessageType.CREATE_APPROVE_GROUP.equals(code)){
            type = MessageType.FLOW_LOG_GROUP;
        }
        //是否需要审批
        boolean needApproveFlg = true;
        String groupLevel = groupJson.getString("levels");//密级
        //如果是非密
        if(MessageType.NO_SECRECT_LEVEL.equals(groupLevel)){
            //如果是非密不需要审批
            needApproveFlg = false;
        }
        String approveList = groupJson.getString("approveList")==null?"":groupJson.getString("approveList");
        groupJson.put("groupId",groupId);
        groupJson.put("creatorIp",userIp);
        groupJson.put("creatorNo",userNo);
        jsonObject.put("data",groupJson);
        msg = jsonObject.toJSONString();
        //记录流水日志
        ZzGroupStatus zzGroupStatus = getStatus(userName,userId,MessageType.FLOW_NEW,groupId,type);
        JSONArray memberJsonArray = JSONObject.parseArray(groupJson.getString("userList"));
        String[] memberArray = this.dealUserIdsAndNames(memberJsonArray);
        /**群成员id*/
        String memberIds = memberArray[0];
        /*//群成员名称*/
        String memberNames = memberArray[1];
        String desPart2 = "";
        if(type.equals(MessageType.FLOW_LOG_GROUP)){
            desPart2 =  "创建了群："+ groupJson.getString("groupName")+"；群成员："+memberNames+"；群成员id："+memberIds+"；操作结果：创建成功";
        }else if(type.equals(MessageType.FLOW_LOG_MEET)){
            desPart2 =  "创建了会议："+ groupJson.getString("groupName")+"；会议成员："+memberNames+"；会议成员id："+memberIds+"；操作结果：创建成功";
        }
        zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+ desPart2);
        //记录审批日志
        String[] approveArray = this.dealApproverIdsAndNames(approveList,userId,userName);
        /**审批人id*/
        String approveIds = approveArray[0];
        /*//审批人名称*/
        String approveNames = approveArray[1];
        ZzGroupApproveLog approveLog = getApproveLog(approveIds,approveNames,userIp,groupJson,MessageType.FLOW_NEW,userNo,userName,type);
        String id = RandomId.getUUID();
        try {
            ZzGroupApprove zzGroupApprove = getApproveInf(msg,id,groupId,approveList,userId,userName,groupJson.getString("groupName"),type);
            int i = this.zzGroupApproveDao.add(zzGroupApprove);
        }catch (Exception e){
            log.error("新建群（会议或其他资源）报错："+ Common.getExceptionMessage(e));
            res =  ResConst.INT_RES_FAIL;
            String desPart22 = "";
            if(MessageType.FLOW_LOG_GROUP.equals(type)){
                desPart22 =  "创建了群："+ groupJson.getString("groupName")+"；群成员："+memberNames+"；群成员id："+memberIds+"；操作结果：创建失败；失败原因：系统出错";
            }else if(MessageType.FLOW_LOG_MEET.equals(type)){
                desPart22 =  "创建了会议："+ groupJson.getString("groupName")+"；会议成员："+memberNames+"；会议成员id："+memberIds+"；操作结果：创建失败；失败原因：系统出错";
            }
            zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+desPart22);
            approveLog.setStatus("0");
        }

        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
        rabbitMqMsgProducer.sendMsgGroupApproveLog(approveLog);
        if(res == ResConst.INT_RES_FAIL){
            return res;
        }
        ChatAdminUserVo userInfo = iUserService.getUserInfo(userId);
        int require_approve_authority = zzRequireApproveAuthorityService.needApprove(userInfo.getOrgCode());
        //无需审批(1特殊权限2非密)，直接审批通过，生成会议或者群组。注:该功能与本类approve的逻辑保持一致。
        if(((MessageType.NO_REQUIRE_APPROVE_AUTHORITY == require_approve_authority) || !needApproveFlg )&& res==ResConst.INT_RES_SUCCESS){
            Map<String,String> approveParam = new HashMap<>(16);
            //数据id
            approveParam.put("id",id);
            //审批通过
            approveParam.put("approveFlg","1");
            approveParam.put("userId",userId);
            approveParam.put("userName",userName);
            approveParam.put("ip","");
            Map<String,String> approveResMap = this.approve(approveParam);
        }
        return res;
    }

    /**
     * 审批数据
     * @param msg
     * @param id
     * @param groupId
     * @param approveList
     * @param userId
     * @param userName
     * @param groupName
     * @param type
     * @return
     */
    private ZzGroupApprove getApproveInf(String msg,String id,String groupId,String approveList,String userId,String userName,String groupName,String type){
        ZzGroupApprove zzGroupApprove  = new ZzGroupApprove();
        zzGroupApprove.setMsg(msg);
        zzGroupApprove.setId(id);
        zzGroupApprove.setGroupId(groupId);
        zzGroupApprove.setApproveList(approveList);
        zzGroupApprove.setCreator(userId);
        zzGroupApprove.setCreatorName(userName);
        zzGroupApprove.setGroupName(groupName);
        zzGroupApprove.setType(type);
        return zzGroupApprove;
    }
    /**
     * 获取审批日志
     * @param approveIds
     * @param approveNames
     * @param userIp
     * @param groupJson
     * @param operateType
     * @param userNo
     * @param userName
     * @param type
     * @return
     */
    private ZzGroupApproveLog getApproveLog(String approveIds,String approveNames,String userIp,JSONObject groupJson,String operateType,String userNo,String userName,String type ){
        ZzGroupApproveLog approveLog = new ZzGroupApproveLog();
        approveLog.setId(RandomId.getUUID());
        approveLog.setApprove(Common.nulToEmptyString(approveIds));
        approveLog.setApproveName(Common.nulToEmptyString(approveNames));
        approveLog.setIp(userIp);
        approveLog.setApproveRes("");
        approveLog.setGroupId(Common.nulToEmptyString(groupJson.getString("groupId")));
        approveLog.setGroupName(Common.nulToEmptyString(groupJson.getString("groupName")));
        approveLog.setGroupDes(Common.nulToEmptyString(groupJson.getString("groupDescribe")));
        approveLog.setGroupLevel(Common.nulToEmptyString(groupJson.getString("levels")));
        approveLog.setGroupPro(Common.nulToEmptyString(groupJson.getString("pname")));
        approveLog.setGroupScope(Common.nulToEmptyString(groupJson.getString("scop")));
        approveLog.setGroupType(Common.nulToEmptyString(groupJson.getString("groupType")));
        approveLog.setStatus("1");
        approveLog.setOperateType(operateType);
        approveLog.setOperator(Common.nulToEmptyString(groupJson.getString("creator")));
        approveLog.setOperatorNo(userNo);
        approveLog.setOperatorName(userName);
        approveLog.setOperateTime(new Date());
        approveLog.setType(type);
        return approveLog;
    }
    /**
     * 群或者会议流水创建
     * @param userName
     * @param userId
     * @param groupId
     * @param type
     * @return
     */
    private ZzGroupStatus getStatus(String userName,String userId,String operateType,String groupId,String type){
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(Common.nulToEmptyString(userName));
        zzGroupStatus.setOperator(Common.nulToEmptyString(userId));
        //创建群
        zzGroupStatus.setOperateType(operateType);
        zzGroupStatus.setGroupId(groupId);
        zzGroupStatus.setOperateTime(new Date());
        zzGroupStatus.setType(type);
        return zzGroupStatus;
    }

    /**
     * 合并用户id和name 返回第一个是id，第二个是name
     * @param memberJsonArray
     * @return
     */
    private String[] dealUserIdsAndNames(JSONArray memberJsonArray){
        /**群成员id*/
        String memberIds = "";
        /*//群成员名称*/
        String memberNames = "";
        if(memberJsonArray!=null){
            for(int i = 0;i<memberJsonArray.size();i++){
                JSONObject memberJson = JSONObject.parseObject(memberJsonArray.getString(i));
                String memberId = memberJson.getString("userId");
                String memberName = memberJson.getString("userName");
                memberIds += ","+memberId;
                memberNames += ","+memberName;
            }
        }
        if(!"".equals(memberIds)){
            memberIds = memberIds.substring(1);
            memberNames = memberNames.substring(1);
        }
        return new String[]{memberIds,memberNames};
    }

    /**
     * 合并用户id和name 返回第一个是id，第二个是name
     * @param approveList
     * @param defaultId
     * @param defaultName
     * @return
     */
    private String[] dealApproverIdsAndNames(String approveList,String defaultId,String defaultName){
        /**审批人id*/
        String approveIds = "";
        /*//审批人名称*/
        String approveNames = "";
        if(!"".equals(approveList)){
            JSONArray approveJsonArray = JSONObject.parseArray(approveList);
            if(approveJsonArray!=null){
                for(int i = 0;i<approveJsonArray.size();i++){
                    JSONObject approveJson = JSONObject.parseObject(approveJsonArray.getString(i));
                    String approveId = approveJson.getString("userId");
                    String approveName = approveJson.getString("userName");
                    String approveNo = approveJson.getString("userNo");
                    approveIds += ","+approveId;
                    approveNames += ","+approveName;
                }
            }
        }
        if(!"".equals(approveIds)){
            approveIds = approveIds.substring(1);
            approveNames = approveNames.substring(1);
        }else {
            approveIds = defaultId;
            approveNames = defaultName;
        }
        return new String[]{approveIds,approveNames};
    }
    /**
     * 发送审批消息，记录到日志表
     * @param resMap
     * @param userName
     * @param userId
     */
    private void sendApproveLog(Map<String,String> resMap,String userName,String userId){
        //记录群状态变动begin
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(Common.nulToEmptyString(userName));
        zzGroupStatus.setOperator(Common.nulToEmptyString(userId));
        zzGroupStatus.setOperateTime(new Date());
        //群审批
        zzGroupStatus.setOperateType(MessageType.FLOW_APPROVE);
        //记录群状态变动end
        String type = Common.nulToEmptyString(resMap.get("type"));
        zzGroupStatus.setGroupId(resMap.get("groupId"));
        zzGroupStatus.setType(type);
        String decPart2 = "";
        if(MessageType.FLOW_LOG_GROUP.equals(type)){
            decPart2 = "审批了群："+resMap.get("groupName")+",审批结论："+resMap.get("approveRes")+"，审批结果：审批成功";
        }
        if(MessageType.FLOW_LOG_MEET.equals(type)){
            decPart2 = "审批了会议："+resMap.get("groupName")+",审批结论："+resMap.get("approveRes")+"，审批结果：审批成功";
        }
        zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+decPart2);
        if(resMap.get("res").equals("0")){
            String decPart22 = "";
            if(MessageType.FLOW_LOG_GROUP.equals(type)){
                decPart22 = "审批了群："+resMap.get("groupName")+",审批结论："+resMap.get("approveRes")+"，审批结果：审批失败，失败原因：记录已经由其他人审批";
            }
            if(MessageType.FLOW_LOG_MEET.equals(type)){
                decPart22 = "审批了会议："+resMap.get("groupName")+",审批结论："+resMap.get("approveRes")+"，审批结果：审批失败，失败原因：记录已经由其他人审批";
            }
            zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+decPart22);
        }
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
    }
    /**
     * 审批返回0 已经审批
     * @param params
     * @return
     */
    @Transactional
    @Override
    public Map<String,String> approve(Map params){
        ZzGroupApproveLog approveLog = new ZzGroupApproveLog();
        ChatAdminUserVo userInfo0 = iUserService.getUserInfo(params.get("userId").toString());
        params.put("userNo", Common.nulToEmptyString(userInfo0.getPId()));
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
        Clob clob = (Clob)singleInf.get("MSG");
        String msg = "";
        List<String> teamUserList = null;
        String type = "";//类型：或者0代表群，1会议
        try {
            msg = Common.ClobToString(clob);
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("data");
            JSONObject groupJson = JSONObject.parseObject(message);
            if((MessageType.CREATE_APPROVE_MEET).equals(code)){
                //会议
                type = MessageType.FLOW_LOG_MEET;
            }
            if((MessageType.CREATE_APPROVE_GROUP).equals(code)){
                //群
                type = MessageType.FLOW_LOG_GROUP;
            }
            //如果是审批通过，且审批结果正常
            if("1".equals(approveFlg)&&res.get("res").equals("1")){
                String groupId = groupJson.getString("groupId");
                //如果是群
                if(MessageType.FLOW_LOG_GROUP.equals(type)){
                    ObjectRestResponse restResponse = zzGroupService.createGroup(message);
                    //如果建群正常，发送socket
                    if(restResponse.isRel()){
                        teamUserList = this.zzGroupService.queryGroupUserIdListByGroupId(groupId);
                        int groupCreateType = 0;
                        GroupTaskDto groupTaskDto = this.zzGroupService.getSendSocketGroupInf(groupId,teamUserList,groupCreateType);
                        sendTeamBindMsgAfterCreate(groupId,teamUserList,groupTaskDto,SocketMsgDetailTypeEnum.GROUP_CREATE_ACK);
                        addCache(teamUserList,groupId,CacheConst.userGroupIds);
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
                    MeetingVo meeting = zzMeetingService.queryById(groupId);
                    teamUserList = this.zzMeetingService.listMeetUserIds(groupId);
                    sendTeamBindMsgAfterCreate(groupId,teamUserList,meeting,SocketMsgDetailTypeEnum.MEETING_ADD);
                    addCache(teamUserList,groupId,CacheConst.userMeetIds);
                }
            }
            if(type==null || type.equals("")){
                //默认是群
                type = MessageType.FLOW_LOG_GROUP;
            }
            //记录审批日志
            putApproveLogInf(approveLog,params,groupJson,ifApproveFlg,res,type,userInfo0);
            res.put("groupId",groupJson.getString("groupId"));
            res.put("groupName",groupJson.getString("groupName"));
            res.put("type",type);
        } catch (Exception e){
            log.error(Common.getExceptionMessage(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(teamUserList!=null && !teamUserList.isEmpty()){
                removeCache(teamUserList,(MessageType.FLOW_LOG_GROUP.equals(type))?CacheConst.userGroupIds:CacheConst.userMeetIds);
            }
        }finally {
            if(approveLog.getGroupId()!=null){
                this.rabbitMqMsgProducer.sendMsgGroupApproveLog(approveLog);
            }
        }
        sendApproveLog(res,params.get("userName").toString(),params.get("userId").toString());
        return res;
    }

    /**
     * 设置审批日志信息
     * @param approveLog
     * @param params
     * @param groupJson
     * @param ifApproveFlg
     * @param res
     * @param type
     * @param userInfo0
     */
    private void putApproveLogInf(ZzGroupApproveLog approveLog,Map params,JSONObject groupJson,String ifApproveFlg,Map<String,String> res,String type,ChatAdminUserVo userInfo0){
        approveLog.setId(RandomId.getUUID());
        approveLog.setApprove("");
        approveLog.setApproveName("");
        approveLog.setIp(Common.nulToEmptyString(params.get("ip")));
        approveLog.setApproveRes(Common.nulToEmptyString(params.get("approveFlg")));
        approveLog.setGroupId(Common.nulToEmptyString(groupJson.getString("groupId")));
        approveLog.setGroupName(Common.nulToEmptyString(groupJson.getString("groupName")));
        approveLog.setGroupDes(Common.nulToEmptyString(groupJson.getString("groupDescribe")));
        approveLog.setGroupLevel(Common.nulToEmptyString(groupJson.getString("levels")));
        approveLog.setGroupPro(Common.nulToEmptyString(groupJson.getString("pname")));
        approveLog.setGroupScope(Common.nulToEmptyString(groupJson.getString("scop")));
        approveLog.setGroupType(Common.nulToEmptyString(groupJson.getString("groupType")));
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
    }
    /**
     * 创建群或者用户后，发送消息，进行绑定（消息嵌套，第一层是绑定，绑定完成后给成员发详细信息）
     * @param teamId 群体id
     * @param userList 绑定列表
     * @param message 绑定完成后发送消息
     * @param msgCode 业务消息码
     */
    private void sendTeamBindMsgAfterCreate(String teamId,List<String> userList,Object message,SocketMsgDetailTypeEnum msgCode){
        //绑定消息体
        SocketMsgVo bindMsgVo = new SocketMsgVo();
        bindMsgVo.setCode(SocketMsgTypeEnum.BIND_USER);
        //绑定详细vo
        SocketMsgDetailVo bindDetailVo = new SocketMsgDetailVo();
        bindDetailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
        //绑定数据
        SocketTeamBindVo bindVo  = new SocketTeamBindVo();
        bindVo.setTeamId(teamId);
        bindVo.setUserList(userList);

        //绑定完成后发送消息
        SocketMsgDetailVo teamDetailVo = new SocketMsgDetailVo();
        teamDetailVo.setCode(msgCode);
        //成员接收的消息
        teamDetailVo.setData(message);
        bindVo.setMsg(teamDetailVo);
        //绑定完成后全量发送
        bindVo.setWholeFlg(true);

        //绑定后发送消息给群成员
        bindDetailVo.setData(bindVo);
        bindMsgVo.setMsg(bindDetailVo);
        rabbitMqMsgProducer.sendSocketMsg(bindMsgVo);
    }
    /**
     * 添加缓存
     * @param userList
     * @param teamId
     * @param teamType
     */
    private void addCache(List<String> userList,String teamId,String teamType){
        for (int i = 0; i < userList.size(); i++) {
            String user = userList.get(i);
            //redis 缓存处理 把用户的群列表缓存更新
            String key = teamType+":"+user;
            boolean keyExist = RedisUtil.isKeyExist(key);
            //如果key存在更新缓存，把最新的数据加入进去
            if(keyExist){
                RedisListUtil.putSingleWithoutDup(key,teamId);
            }

        }

    }

    /**
     * 删除缓存
     * @param userList
     * @param teamType
     */
    private void removeCache(List<String> userList,String teamType){
        for (int i = 0; i < userList.size(); i++) {
            String user = userList.get(i);
            //redis 缓存处理 把用户的群列表缓存更新
            String key = teamType+":"+user;
            boolean keyExist = RedisUtil.isKeyExist(key);
            //如果key存在更新缓存，把脏数据删除
            if(keyExist){
                RedisUtil.remove(key);
            }
        }
    }
    //查询我的申请群列表
    @Override
    public TableResultResponse<GroupApplyVo> getApplyGroupList(Map params) throws Exception{
        List<GroupApplyVo> dataList = new ArrayList<>();
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(Common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(Common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
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
            String msgtemp = Common.ClobToString(msglb);
            JSONObject jsonObject = JSONObject.parseObject(msgtemp);
            String message = jsonObject.getString("data");
            JSONObject groupJson = JSONObject.parseObject(message);
            GroupApplyVo groupApplyVo = new GroupApplyVo();
            groupApplyVo.setId(Common.nulToEmptyString(temp.get("ID")));
            groupApplyVo.setApply(Common.nulToEmptyString(groupJson.getString("creator")));
            groupApplyVo.setApplyName(Common.nulToEmptyString(groupJson.getString("creatorName")));
            groupApplyVo.setApplyTime(Common.nulToEmptyString(temp.get("CREATETIME")));
            String approveFlg = Common.nulToEmptyString(temp.get("APPROVEFLG"));
            String type = Common.nulToEmptyString(temp.get("TYPE"));
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
                groupApplyVo.setApprove(Common.nulToEmptyString(temp.get("APPROVE")));
                groupApplyVo.setApproveName(Common.nulToEmptyString(temp.get("APPROVENAME")));
                groupApplyVo.setApproveTime(Common.nulToEmptyString(temp.get("APPROVETIME")));
            }
            groupApplyVo.setApproveStatus(approveFlg);
            groupApplyVo.setGroupDescribe(Common.nulToEmptyString(groupJson.getString("groupDescribe")));
            groupApplyVo.setGroupId(Common.nulToEmptyString(groupJson.getString("groupId")));
            groupApplyVo.setGroupLevel(Common.nulToEmptyString(groupJson.getString("levels")));
            groupApplyVo.setGroupName(Common.nulToEmptyString(groupJson.getString("groupName")));
            groupApplyVo.setGroupScope(Common.nulToEmptyString(groupJson.getString("scop")));
            groupApplyVo.setGroupPro(Common.nulToEmptyString(groupJson.getString("pname")));
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
            msgStr = Common.ClobToString(clob);
        } catch (SQLException e) {
            log.error(Common.getExceptionMessage(e));
        } catch (IOException e) {
            log.error(Common.getExceptionMessage(e));
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
            msgStr = Common.ClobToString(clob);
        } catch (SQLException e) {
            log.error(Common.getExceptionMessage(e));
        } catch (IOException e) {
            log.error(Common.getExceptionMessage(e));
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
            pageNum=Integer.valueOf(Common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(Common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Map> inList = this.zzGroupApproveDao.getApproveList(params);
        PageInfo pageInfo = new PageInfo<>(inList);

        String userId = Common.nulToEmptyString(params.get("userId"));
        for(Map temp:inList){
            Clob msglb = (Clob)temp.get("MSG");
            String msgtemp = Common.ClobToString(msglb);
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
            String type = Common.nulToEmptyString(temp.get("TYPE"));
            GroupApproveVo groupApproveVo = new GroupApproveVo();
            groupApproveVo.setId(temp.get("ID").toString());
            groupApproveVo.setCreateTime(temp.get("CREATEDATE").toString());
            //UserInfo userInfo = iUserService.info(groupJson.getString("creator"));
            groupApproveVo.setCreatorName(Common.nulToEmptyString(groupJson.getString("creatorName")));
            groupApproveVo.setGroupDescribe(Common.nulToEmptyString(groupJson.getString("groupDescribe")));
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
