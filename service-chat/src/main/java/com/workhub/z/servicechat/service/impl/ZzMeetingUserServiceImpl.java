package com.workhub.z.servicechat.service.impl;


import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.google.common.base.Joiner;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzMeetingUserDao;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.entity.meeting.ZzMeetingUser;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzMeetingService;
import com.workhub.z.servicechat.service.ZzMeetingUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:zhuqz
 * description:会议用户管理
 * date:2019/9/20 15:00
 **/
@Service("zzMeetingUserService")
public class ZzMeetingUserServiceImpl implements ZzMeetingUserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzMeetingUserDao zzMeetingUserDao;
    @Resource
    private ZzMeetingService zzMeetingService;
    @Autowired
    private AdminUserService iUserService;
    @Resource
    private RabbitMqMsgProducer rabbitMqMsgProducer;
    /**添加成员单个*/
    @Override
    public  int addUser(ZzMeetingUser zzMeetingUser){
        zzMeetingUser.setId(RandomId.getUUID());
        try {
            String canMsg = "1";
            if(zzMeetingUser.getCanMsg()==null || "".equals(zzMeetingUser.getCanMsg())){
                zzMeetingUser.setCanMsg(canMsg);
            }
            Common.putVoNullStringToEmptyString(zzMeetingUser);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
        }
        return this.zzMeetingUserDao.addUser(zzMeetingUser);
    }
    /**删除某个成员*/
    @Override
    public int delUser(String meetId, String userId){
        return this.zzMeetingUserDao.delUser(meetId,userId);
    }
    /**获取会议的全部成员*/
    @Override
    public ListRestResponse queryMeetAllUsers(String meetId){
        List<Map> userList = this.zzMeetingUserDao.getMeetAllUsers(meetId);
        List<MeetUserVo> meetUserList = this.dealMeetUserList(userList);
        return new ListRestResponse("200",meetUserList.size(),meetUserList);
    }

    /**整理会议人员信息*/
    public List<MeetUserVo> dealMeetUserList(List<Map> oriList){
        List<MeetUserVo> meetUserList = new ArrayList<>();
        for(Map map : oriList){
            MeetUserVo meetUserVo = new MeetUserVo();
            ChatAdminUserVo userInfo = iUserService.getUserInfo(Common.nulToEmptyString(map.get("USERID")));
            if(userInfo == null ){
                userInfo = new ChatAdminUserVo();
            }
            meetUserVo.setUserId(Common.nulToEmptyString(map.get("USERID")));
            meetUserVo.setUserName(Common.nulToEmptyString(map.get("USERNAME")));
            meetUserVo.setUserNo(Common.nulToEmptyString(map.get("USERNO")));
            meetUserVo.setUserRoleCode(Common.nulToEmptyString(map.get("ROLECODE")));
            meetUserVo.setUserRoleName(Common.nulToEmptyString(map.get("ROLENAME")));
            meetUserVo.setJoinTime(Common.nulToEmptyString(map.get("CRTTIME")));
            meetUserVo.setUserLevel(Common.nulToEmptyString(map.get("USERLEVEL")));
            meetUserVo.setCanMsg(Common.nulToEmptyString(map.get("CANMSG")));
            String online = Common.nulToEmptyString(RedisUtil.getValue(CacheConst.userOnlineCahce+ Common.nulToEmptyString(map.get("USERID"))));
            meetUserVo.setOnline((MessageType.ONLINE+"").equals(online)?"1":")");
            meetUserVo.setUserLevel(Common.nulToEmptyString(userInfo.getSecretLevel()));
            meetUserVo.setUserImg(Common.nulToEmptyString(userInfo.getAvatar()));
            meetUserVo.setUserOrgCode(Common.nulToEmptyString(userInfo.getOrgCode()));
            meetUserVo.setUserOrgName(Common.nulToEmptyString(userInfo.getOrgName()));
            meetUserVo.setPathName(Common.nulToEmptyString(userInfo.getPathName()));
            meetUserList.add(meetUserVo);
        }
        try {
            Common.putVoNullStringToEmptyString(meetUserList);
            //排序 在线排在前面
            meetUserList = meetUserList.stream().sorted(Comparator.comparing(MeetUserVo::getOnline).reversed()).collect(Collectors.toList());
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
        }
        return meetUserList;
    }

/**更新成员*/
    @Override
    public int updateUser(ZzMeetingUser meetingUser){
        int i = this.zzMeetingUserDao.updateUser(meetingUser);
        zzMeetingService.pushNewMeetInfToSocket(meetingUser.getUpdUser(),meetingUser.getMeetingId());
        return i;
    }
    /**
     * @MethodName: updateUserList
     * 更新用户列表
     * @param: meetingUsers 用户列表
     * @return: int
     * @Author: zhuqz
     * @Date: 2019/10/21
     **/
    @Override
    public int updateUserList(List<ZzMeetingUser> meetingUsers){
        int i = this.zzMeetingUserDao.updateUserList(meetingUsers);
        zzMeetingService.pushNewMeetInfToSocket(meetingUsers.get(0).getUpdUser(),meetingUsers.get(0).getMeetingId());
        return  i;
    }
/**批量添加成员*/
    @Override
    public int addListUsers(List<ZzMeetingUser> users){
        for(ZzMeetingUser zzMeetingUser:users){
            String canMsg = "1";
            if(zzMeetingUser.getCanMsg()==null || "".equals(zzMeetingUser.getCanMsg())){
                zzMeetingUser.setCanMsg(canMsg);
            }
        }
        try {
            Common.putVoNullStringToEmptyString(users);
        }catch (Exception e){
            logger.error("批量添加成员出错");
            logger.error(Common.getExceptionMessage(e));
        }
        int i = this.zzMeetingUserDao.addListUsers(users);
        return i;
    }
    /**删除会议所有人员*/
    @Override
    public int delMeetAllUser(String meetId){
        return this.zzMeetingUserDao.delMeetAllUser(meetId);
    }
    /**查询会议所有人员，供给系统内部方法调用*/
    @Override
    public List<MeetUserVo> queryMeetAllUsersVo(String meetId){
        List<Map> userList = this.zzMeetingUserDao.getMeetAllUsers(meetId);
        return this.dealMeetUserList(userList);
    }

    /**查询人员所有会议，供给系统内部方法调用*/
    @Override
    //@Cacheable(value = CacheConst.userMeetIds,key = "#p0")
    public List<String> getMeetingByUserId(String userId){
        List<String> meetList = null;
        //是否含有key
        String key = CacheConst.userMeetIds+":"+userId;
        boolean keyExist = RedisUtil.isKeyExist(key);
        //todo 暂时不走缓存
        keyExist = false;
         if(keyExist){
                //如果含有key 直接返回
                return RedisListUtil.getList(key);
        }else {
            //如果不含有key ，进行缓存
            meetList = this.zzMeetingUserDao.getMeetingByUserId(userId);
            if(meetList!=null && meetList.size()>0){
                RedisListUtil.putList(key,meetList);
            }

        }
        return meetList;
    }

    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    /**
     * 编辑会议人员 轻易不要捕捉异常，因为开启了事务
     * @param meetingVo
     * @param userId 操作人id
     * @param userName 操作人姓名
     * @param userNo 操作人身份证
     * @param userIp 操作人ip
     * @return 1成功 -1失败 3校验失败
     */
    public int editMeetUser(MeetingVo meetingVo, String userId, String userName,String userNo,String userIp){
        int success = 1,error = -1,validErrorNotInGroup = 3;
        int operatorNotInGroup = 0;
        String meetId = meetingVo.getId();
        int valid = meetMemberEditValidate(meetId,userId);
        if(valid == operatorNotInGroup){
            return  validErrorNotInGroup;
        }
        List<ChatAdminUserVo> addUserInfoList = null;
        List<ChatAdminUserVo> removeUserInfoList = null;
        try {
            List<MeetUserVo> nowUserList = meetingVo.getMeetMemberList();
            List<String> nowUserListStr = new ArrayList<>();
            for(MeetUserVo vo:nowUserList){
                nowUserListStr.add(Common.nulToEmptyString(vo.getUserId()));
            }
            List<String> userListStr = this.zzMeetingService.listMeetUserIds(meetId);
            TeamMemberChangeListVo memberChangeListVo = Common.teamMemberChangeInf(userListStr,nowUserListStr);
            List<String> addUserList = memberChangeListVo.getAddList();
            List<String> delUserList = memberChangeListVo.getDelList();
            List<String> noMoveList = memberChangeListVo.getNoMoveList();
            //处理群成员begin
            //添加
            meetUserEditAdd(meetId,userId,userName,userNo,userIp,addUserList,nowUserList);
            //删除
            meetUserEditDelete(meetId,delUserList);
            //处理群成员end
            //如果有新增人员 发送消息
            if(addUserList!=null && !addUserList.isEmpty()){
                addUserInfoList = iUserService.userList(Joiner.on(",").join(addUserList));
                String[] addCacheInfBack = dealRedisCacheAdd(meetId,addUserInfoList);
                String addIds = addCacheInfBack[0];
                String addNames = addCacheInfBack[1];
                sendSocketMsgAdd(meetId,addUserList);
                //记录会议变更流水
                sendMeetAddUserInf(meetId,userId,userName,addIds,addNames);
            }
            //如果有删除人员发送消息
            if(delUserList!=null && !delUserList.isEmpty()){
                removeUserInfoList = iUserService.userList(Joiner.on(",").join(delUserList));
                String[] delCacheInfBack = dealRedisCacheDelete(meetId,removeUserInfoList);
                String delIds = delCacheInfBack[0];
                String delNames = delCacheInfBack[1];
                //发送会议绑定消息
                sendSocketMsgDelete(meetId,delUserList);
                //发送会议流水消息
                sendMeetDeleteUserInf(meetId,userId,userName,delIds,delNames);
            }
            if((addUserList!=null  && !addUserList.isEmpty()) || (delUserList!=null && !delUserList.isEmpty())){
                //发送给未移动人员，通知群变动
                sendSocketMsgNoMove(meetId,noMoveList);
            }
        } catch (Exception e) {
            logger.error("编辑会议人员出错!!!");
            logger.error(Common.getExceptionMessage(e));
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(addUserInfoList!=null){
                deleteDirtyCacheIfError(addUserInfoList);
            }
            if(removeUserInfoList!=null){
                deleteDirtyCacheIfError(removeUserInfoList);
            }
            return error;
        }
        return success;
    }
    /**
     * 会议人员编辑校验
     * @param meetId
     * @param userId
     * @return 1校验通过 0 操作人不在群组
     */
    private int meetMemberEditValidate(String meetId,String userId){
        int success = 1,operatorNotInGroup = 0;
        List<String> memberList = this.zzMeetingService.listMeetUserIds(meetId);
        if(!memberList.contains(userId)){
            return operatorNotInGroup;
        }
        return success;
    }

    /**
     * 会议编辑人员新增
     * @param meetId
     * @param userId
     * @param userName
     * @param userNo
     * @param userIp
     * @param addUserList
     * @param nowUserList
     */
    private void meetUserEditAdd(String meetId,String userId,String userName,String userNo,String userIp,List<String> addUserList,List<MeetUserVo> nowUserList ){
        if(addUserList!=null && !addUserList.isEmpty()){
            List<ZzMeetingUser> userGroupList = new ArrayList<>();
            for(int i=0;i<addUserList.size();i++){
                MeetUserVo userVo = null;
                for(int j=0;j<nowUserList.size();j++){
                    if(addUserList.get(i).equals(nowUserList.get(j).getUserId())){
                        userVo = nowUserList.get(j);
                    }
                }
                ChatAdminUserVo userInfo = this.iUserService.getUserInfo(addUserList.get(i));
                ZzMeetingUser zzMeetingUser = new ZzMeetingUser();
                zzMeetingUser.setId(RandomId.getUUID());
                zzMeetingUser.setMeetingId(meetId);
                zzMeetingUser.setUserId(addUserList.get(i));
                zzMeetingUser.setCanMsg((userVo.getCanMsg()==null||"".equals(userVo.getCanMsg()))?"1":userVo.getCanMsg());
                zzMeetingUser.setRoleCode(userVo.getUserRoleCode());
                zzMeetingUser.setUserName(userInfo.getName());
                zzMeetingUser.setUserNo(userInfo.getPId());
                zzMeetingUser.setUserLevel(userInfo.getSecretLevel());
                zzMeetingUser.setCrtUser(userId);
                zzMeetingUser.setCrtHost(userIp);
                zzMeetingUser.setCrtName(userName);
                zzMeetingUser.setCrtNo(userNo);
                userGroupList.add(zzMeetingUser);
            }
            this.zzMeetingUserDao.addListUsers(userGroupList) ;
        }
    }

    /**
     * 会议编辑人员删除
     * @param meetId
     * @param delUserList
     */
    private void meetUserEditDelete(String meetId,List<String> delUserList){
        if(delUserList!=null && !delUserList.isEmpty()){
            List<ZzMeetingUser> userGroupList = new ArrayList<>();
            for(int i=0;i<delUserList.size();i++){
                ZzMeetingUser zzMeetingUser = new ZzMeetingUser();
                zzMeetingUser.setMeetingId(meetId);
                zzMeetingUser.setUserId(delUserList.get(i));
                userGroupList.add(zzMeetingUser);
            }
            this.zzMeetingUserDao.delListUsers(userGroupList) ;
        }
    }
    /**
     * 添加会议人员处理缓存
     * @param meetId
     * @param addUserInfoList
     * @return
     */
    private String[] dealRedisCacheAdd(String meetId,List<ChatAdminUserVo> addUserInfoList){

        String userNames = "";
        String userIds = "";
        for (ChatAdminUserVo userInfo:addUserInfoList){
            userNames += ","+userInfo.getName();
            userIds += ","+userInfo.getId();
            //redis 缓存处理 把用户的会议列表缓存更新
            String key = CacheConst.userMeetIds+":"+userInfo.getId();
            boolean keyExist = RedisUtil.isKeyExist(key);
            //如果key存在更新缓存，把最新的数据加入进去
            if(keyExist){
                RedisListUtil.putSingleWithoutDup(key,meetId);
            }

        }
        if(!userNames.equals("")){
            userNames = userNames.substring(1);
            userIds = userIds.substring(1);
        }
        return  new String[]{userIds,userNames};
    }
    /**
     * 添加群成员给添加群成员的人发送消息
     * @param meetId
     * @param users
     */
    private void sendSocketMsgAdd(String meetId,List<String> users){
        SocketMsgVo msgVo = new SocketMsgVo();
        msgVo.setCode(SocketMsgTypeEnum.BIND_USER);
        msgVo.setSender("");
        msgVo.setReceiver("");
        SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
        socketTeamBindVo.setTeamId(meetId);
        socketTeamBindVo.setUserList(users);
        SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
        detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);

        //绑定完成后发送新建会议的消息给新加入的人
        SocketMsgDetailVo groupDetailVo = new SocketMsgDetailVo();
        groupDetailVo.setCode(SocketMsgDetailTypeEnum.MEETING_ADD);
        MeetingVo meetingVo = this.zzMeetingService.queryById(meetId);
        groupDetailVo.setData(meetingVo);
        socketTeamBindVo.setMsg(groupDetailVo);
        //需要增量发送，因为会议里有些人是不动的，不能都给他们发群创建消息
        socketTeamBindVo.setWholeFlg(false);

        detailVo.setData(socketTeamBindVo);
        msgVo.setMsg(detailVo);
        rabbitMqMsgProducer.sendSocketMsg(msgVo);
    }
    /**
     * 群编辑成员，增加时候，记录群流水
     * @param meetId
     * @param operator
     * @param operatorName
     * @param addIs
     * @param addNames
     */
    private void sendMeetAddUserInf(String meetId,String operator,String operatorName,String addIs,String addNames){
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(operatorName);
        zzGroupStatus.setOperator(operator);
        zzGroupStatus.setOperateTime(new Date());
        zzGroupStatus.setOperateType(MessageType.FLOW_ADD_MEMBER);//添加人员
        zzGroupStatus.setGroupId(meetId);
        String describe = zzGroupStatus.getOperatorName()+
                "邀请以下人员加入会议："+addNames+"；人员id："+addIs;
        zzGroupStatus.setDescribe(describe);
        zzGroupStatus.setType(MessageType.FLOW_LOG_MEET);
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
    }
    /**
     * 删除会议人员处理缓存
     * @param meetId
     * @param delUserInfoList
     * @return
     */
    private String[] dealRedisCacheDelete(String meetId,List<ChatAdminUserVo> delUserInfoList){
        String userNames = "";
        String userIds = "";
        for (ChatAdminUserVo userInfo:delUserInfoList){
            userNames += ","+userInfo.getName();
            userIds += ","+userInfo.getId();
            //redis 缓存处理 把用户的群列表缓存更新
            String key = CacheConst.userMeetIds+":"+userInfo.getId();
            boolean keyExist = RedisUtil.isKeyExist(key);
            //如果key存在更新缓存，把最新的数据加入进去
            if(keyExist){
                RedisListUtil.removeSingle(key,meetId);
            }

        }
        if(!userNames.equals("")){
            userNames = userNames.substring(1);
            userIds = userIds.substring(1);
        }
        return  new String[]{userIds,userNames};
    }
    /**
     * 删除会议成员发送消息
     * @param meetId
     * @param users
     */
    private void sendSocketMsgDelete(String meetId,List<String> users){
        SocketMsgVo msgVo2 = new SocketMsgVo();
        msgVo2.setCode(SocketMsgTypeEnum.UNBIND_USER);
        SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
        detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
        SocketTeamBindVo socketTeamBindVo2  = new SocketTeamBindVo();
        socketTeamBindVo2.setTeamId(meetId);
        socketTeamBindVo2.setUserList(users);
        //给每一个被踢出的人发消息，告知被踢出了
        SocketMsgDetailVo outDetailSocketVo = new SocketMsgDetailVo();
        outDetailSocketVo.setCode(SocketMsgDetailTypeEnum.MEETING_QUIT);
        SocketMeetDeleteMemberVo socketMeetDeleteMemberVo = new SocketMeetDeleteMemberVo();
        socketMeetDeleteMemberVo.setMeetId(meetId);
        outDetailSocketVo.setData(socketMeetDeleteMemberVo);
        socketTeamBindVo2.setMsg(outDetailSocketVo);
        //增量发送，因为踢出人员，还有其他人员在会议里，不可能发送全量告诉所有人被t了
        socketTeamBindVo2.setWholeFlg(false);
        detailVo.setData(socketTeamBindVo2);
        msgVo2.setMsg(detailVo);
        rabbitMqMsgProducer.sendSocketMsg(msgVo2);
    }
    /**
     * 会议编辑成员，删除时候，记录会议流水
     * @param meetId
     * @param operator
     * @param operatorName
     * @param delIs
     * @param delNames
     */
    private void sendMeetDeleteUserInf(String meetId,String operator,String operatorName,String delIs,String delNames){
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(operatorName);
        zzGroupStatus.setOperator(operator);
        zzGroupStatus.setOperateType(MessageType.FLOW_DELETE_MEMBER);
        zzGroupStatus.setGroupId(meetId);
        String describe = zzGroupStatus.getOperatorName()+
                "从会议里删除以下人员："+delNames+"；人员id："+delIs;
        zzGroupStatus.setDescribe(describe);
        zzGroupStatus.setOperateTime(new Date());
        zzGroupStatus.setType(MessageType.FLOW_LOG_MEET);
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
    }
    /**
     * 给未移动的人员发送会议变更消息
     * @param meetId
     * @param users
     */
    private void sendSocketMsgNoMove(String meetId,List<String> users){
        //因为要区分出新加人员、未移动人员的消息，避免重复收到消息，这里不能使用群体消息,需要变量未移动人员，发单条消息
        SocketMsgVo groupEditSocketVo = new SocketMsgVo();
        groupEditSocketVo.setCode(SocketMsgTypeEnum.SINGLE_MSG);
        SocketMsgDetailVo groupDetailVo = new SocketMsgDetailVo();
        groupDetailVo.setCode(SocketMsgDetailTypeEnum.MEET_CHANGE);
        MeetingVo meetingVo = this.zzMeetingService.queryById(meetId);
        groupDetailVo.setData(meetingVo);
        groupEditSocketVo.setMsg(groupDetailVo);

        for(String user:users){
            groupEditSocketVo.setReceiver(user);
            rabbitMqMsgProducer.sendSocketMsg(groupEditSocketVo);
        }

    }
    /**
     * 群编辑如果失败，删除可能的脏数据
     * @param userInfos
     */
    private void deleteDirtyCacheIfError(List<ChatAdminUserVo> userInfos){
        for (ChatAdminUserVo userInfo:userInfos){
            //redis 缓存处理 把用户脏数据删除
            String key = CacheConst.userMeetIds+":"+userInfo.getId();
            boolean keyExist = RedisUtil.isKeyExist(key);
            if(keyExist){
                RedisUtil.remove(key);
            }
        }
    }
    /**
     * 获取用户当天需要提醒会议列表
     * @param userId
     * @return
     */
    @Override
    public List<UserCurrentDayMeetJobVo> getUserCurrentDayMeetJob(String userId){
        return this.zzMeetingUserDao.getUserCurrentDayMeetJob(userId);
    }
    /**
     * 查询用户未结束的会议id列表
     * @param userId
     * @return
     */
    @Override
    public List<String> listUserStartingMeetIds(String userId){
        return this.zzMeetingUserDao.listUserStartingMeetIds(userId);
    }
}
