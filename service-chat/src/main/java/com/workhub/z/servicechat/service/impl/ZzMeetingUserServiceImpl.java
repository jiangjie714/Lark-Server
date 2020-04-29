package com.workhub.z.servicechat.service.impl;


import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.google.common.base.Joiner;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzMeetingUserDao;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.entity.meeting.ZzMeetingUser;
import com.workhub.z.servicechat.model.MeetingDto;
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

    /**查询会议所有人员，供给系统内部方法调用*/
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
     * @return 1成功 -1失败 0成员过多
     */
    public int editMeetUser(MeetingVo meetingVo, String userId, String userName,String userNo,String userIp){
        List<ChatAdminUserVo> addUserInfoList = null;
        List<ChatAdminUserVo> removeUserInfoList = null;
        try {
            String meetId = meetingVo.getId();
            List<Map> userList = this.zzMeetingUserDao.getMeetAllUsers(meetId);
            List<MeetUserVo> beforeUserList = this.dealMeetUserList(userList);
            List<MeetUserVo> nowUserList = meetingVo.getMeetMemberList();
            MeetingDto zzMeetNow = zzMeetingService.getMeetInf(meetId);
            if(zzMeetNow == null){
                //会议不存在
                return -1;
            }
            List<String> userListStr = new ArrayList<>();
            for(MeetUserVo vo:beforeUserList){
                userListStr.add(Common.nulToEmptyString(vo.getUserId()));
            }
            List<String> nowUserListStr = new ArrayList<>();
            for(MeetUserVo vo:nowUserList){
                nowUserListStr.add(Common.nulToEmptyString(vo.getUserId()));
            }
            TeamMemberChangeListVo memberChangeListVo = Common.teamMemberChangeInf(userListStr,nowUserListStr);
            List<String> addUserList = memberChangeListVo.getAddList();
            List<String> delUserList = memberChangeListVo.getDelList();
            //处理群成员begin
            //添加
            if(addUserList!=null && addUserList.size()!=0){
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
                    zzMeetingUser.setUserName(userVo.getUserName());
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
            //删除
            if(delUserList!=null && delUserList.size()!=0){
                List<ZzMeetingUser> userGroupList = new ArrayList<>();
                for(int i=0;i<delUserList.size();i++){
                    ZzMeetingUser zzMeetingUser = new ZzMeetingUser();
                    zzMeetingUser.setMeetingId(meetId);
                    zzMeetingUser.setUserId(delUserList.get(i));
                    userGroupList.add(zzMeetingUser);
                }
                this.zzMeetingUserDao.delListUsers(userGroupList) ;
            }
            //处理群成员end
            //MeetingDto zzMeeting = zzMeetingService.getMeetInf(meetId);
            //如果有新增人员 发送消息
            List msgUserList = new ArrayList();
            if(addUserList!=null && addUserList.size()!=0){
                addUserInfoList =  iUserService.userList(Joiner.on(",").join(addUserList));
                String userNames = "";
                String userIds = "";
                for (ChatAdminUserVo userInfo:addUserInfoList){
                    msgUserList.add(userInfo.getId());
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
                SocketMsgVo msgVo = new SocketMsgVo();
                msgVo.setCode(SocketMsgTypeEnum.BIND_USER);
                msgVo.setSender("");
                msgVo.setReceiver("");
                SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
                socketTeamBindVo.setTeamId(meetId);
                socketTeamBindVo.setUserList(msgUserList);
                SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
                detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
                detailVo.setData(socketTeamBindVo);
                msgVo.setMsg(detailVo);
                //校验消息
                CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo);
                //只有消息合法才去绑定socket通信频道
                if(cRes.getRes()){
                    rabbitMqMsgProducer.sendSocketTeamBindMsg(msgVo);
                }
                //记录群状态变动begin
                if(!userNames.equals("")){
                    userNames = userNames.substring(1);
                    userIds = userIds.substring(1);
                }
                ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
                zzGroupStatus.setId(RandomId.getUUID());
                zzGroupStatus.setOperatorName(userName);
                zzGroupStatus.setOperator(userId);
                zzGroupStatus.setOperateTime(new Date());
                zzGroupStatus.setOperateType(MessageType.FLOW_ADD_MEMBER);//添加人员
                zzGroupStatus.setGroupId(meetId);
                //日志类型会议
                zzGroupStatus.setType(MessageType.FLOW_LOG_MEET);
                String describe = zzGroupStatus.getOperatorName()+
                        "邀请以下人员加入会议："+userNames+"；人员id："+userIds;
                zzGroupStatus.setDescribe(describe);
                rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                //记录群状态变动end
            }


            //如果有删除人员发送消息
            List msgUserList2 = new ArrayList();
            if(delUserList!=null && delUserList.size()!=0){
                removeUserInfoList = iUserService.userList(Joiner.on(",").join(delUserList));
                String userNames = "";
                String userIds = "";
                for (ChatAdminUserVo userInfo:removeUserInfoList){
                    msgUserList2.add(userInfo.getId());
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
                SocketMsgVo msgVo2 = new SocketMsgVo();
                msgVo2.setCode(SocketMsgTypeEnum.UNBIND_USER);
                msgVo2.setSender("");
                msgVo2.setReceiver("");
                SocketTeamBindVo socketTeamBindVo2  = new SocketTeamBindVo();
                socketTeamBindVo2.setTeamId(meetId);
                socketTeamBindVo2.setUserList(msgUserList2);
                SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
                detailVo.setData(SocketMsgDetailTypeEnum.DEFAULT);
                detailVo.setData(socketTeamBindVo2);
                msgVo2.setMsg(detailVo);
                //校验消息
                CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo2);
                //只有消息合法才去绑定socket通信频道
                if(cRes.getRes()){
                    rabbitMqMsgProducer.sendSocketTeamUnBindMsg(msgVo2);
                }
                //记录群状态变动begin
                if(!userNames.equals("")){
                    userNames = userNames.substring(1);
                    userIds = userIds.substring(1);
                }
                ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
                zzGroupStatus.setId(RandomId.getUUID());
                zzGroupStatus.setOperatorName(userName);
                zzGroupStatus.setOperator(userId);
                zzGroupStatus.setOperateType(MessageType.FLOW_DELETE_MEMBER);//踢出人员
                zzGroupStatus.setGroupId(meetId);
                //日志类型会议
                zzGroupStatus.setType(MessageType.FLOW_LOG_MEET);
                String describe = zzGroupStatus.getOperatorName()+
                        "从会议里删除以下人员："+userNames+"；人员id："+userIds;
                zzGroupStatus.setDescribe(describe);
                zzGroupStatus.setOperateTime(new Date());
                rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                //记录群状态变动end
            }
        } catch (Exception e) {
            logger.error("编辑会议人员出错!!!");
            logger.error(Common.getExceptionMessage(e));
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(addUserInfoList!=null){
                for (ChatAdminUserVo userInfo:addUserInfoList){
                    //redis 缓存处理 把脏数据删除
                    String key = CacheConst.userMeetIds+":"+userInfo.getId();
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把最新的数据加入进去
                    if(keyExist){
                        RedisUtil.remove(key);
                    }

                }
            }
            if(removeUserInfoList!=null){
                for (ChatAdminUserVo userInfo:removeUserInfoList){
                    //redis 缓存处理 把用户脏数据删除
                    String key = CacheConst.userMeetIds+":"+userInfo.getId();
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把最新的数据加入进去
                    if(keyExist){
                        RedisUtil.remove(key);
                    }

                }
            }

            return -1;
        }
        return 1;
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
}
