package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzMeetingDao;
import com.workhub.z.servicechat.entity.meeting.ZzMeeting;
import com.workhub.z.servicechat.entity.meeting.ZzMeetingUser;
import com.workhub.z.servicechat.model.MeetingDto;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.ZzCodeMeetingRoleService;
import com.workhub.z.servicechat.service.ZzMeetingService;
import com.workhub.z.servicechat.service.ZzMeetingUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.workhub.z.servicechat.config.MessageType.MEET_CHANGE;

/**
 * @author:zhuqz
 * description:会议服务实现类
 * date:2019/9/20 10:58
 **/
@Service("zzMeetingService")
public class ZzMeetingServiceImpl implements ZzMeetingService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzMeetingDao zzMeetingDao;
    @Resource
    private ZzMeetingUserService zzMeetingUserService;
    @Resource
    private ZzCodeMeetingRoleService zzCodeMeetingRoleService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
   /** // 新增*/
    @Override
    public  String add(ZzMeeting zzMeeting){
        zzMeeting.setId(RandomId.getUUID());
        try {
            Common.putVoNullStringToEmptyString(zzMeeting);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
        }

        int i = this.zzMeetingDao.add(zzMeeting);
        return zzMeeting.getId();
    }
    /**查询单条会议信息*/
    @Override
    public MeetingVo queryById(String meetId) {
        MeetingDto meetingDto = this.zzMeetingDao.queryById(meetId);
        MeetingVo meetingVo = new MeetingVo();
        List<MeetRoleCodeVo> rolefunlist = this.getMeetRoleCodeListData();
        try {
            if(meetingDto != null){
                Common.copyObject(meetingDto,meetingVo);
                List<MeetUserVo>  userVos = this.zzMeetingUserService.queryMeetAllUsersVo(meetId);
                for(MeetUserVo user:userVos){
                    String roleCode = user.getUserRoleCode();
                    for(MeetRoleCodeVo meetRoleCodeVo:rolefunlist){
                        if(meetRoleCodeVo.getCode().equals(roleCode)){
                            user.setFunctionList(meetRoleCodeVo.getFunctionList());
                        }
                    }

                }
                meetingVo.setMemberNum(userVos.size()+"");
                meetingVo.setMeetMemberList(userVos);
                this.dealMeetProgress(meetingVo,meetingDto.getAllProgress(),meetingDto.getCurrentProgress());
            }

            Common.putVoNullStringToEmptyString(meetingVo);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
        }
        return meetingVo;
    }
    /**修改*/
    @Override
    public int update(ZzMeeting zzMeeting){
        return  this.zzMeetingDao.update(zzMeeting);
    }
    /**删除*/
    @Override
    @Transactional
    public  int delete(String meetId){
        int delCnt = this.zzMeetingUserService.delMeetAllUser(meetId);
        return this.zzMeetingDao.deleteData( meetId);
    }

    /**创建会议 成功返回会议id,同时rel是true；错误返回错误说明，同时rel 是false*/
    @Override
    @Transactional
    public ObjectRestResponse createMeeting(String meetingJson){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        ZzMeeting zzMeeting = new ZzMeeting();
        try {


            /**返回结果*/
            res.data(zzMeeting.getId());
            /**审批通过*/
            zzMeeting.setMeetStatus(String.valueOf(MessageType.MEETING_APPROVE_PASS));
            /**会议成员*/
            List<ZzMeetingUser> meetUserList = new ArrayList<>();

            JSONObject jsonObject = JSONObject.parseObject(meetingJson);

            String userId = Common.nulToEmptyString(jsonObject.getString("creator"));
            zzMeeting.setCrtUser(userId);
            String userName = Common.nulToEmptyString(jsonObject.getString("creatorName"));
            zzMeeting.setCrtName(userName);
            String userIp = Common.nulToEmptyString(jsonObject.getString("creatorIp"));
            zzMeeting.setCrtHost(userIp);
            String userNo = Common.nulToEmptyString(jsonObject.getString("creatorNo"));

            zzMeeting.setId(jsonObject.getString("groupId"));

            /**会议人员处理*/

            JSONArray userJsonArray = JSONObject.parseArray(jsonObject.getString("userList"));
            if(userJsonArray!=null){
                for(int i = 0;i<userJsonArray.size();i++){
                    ZzMeetingUser zzMeetingUser = new ZzMeetingUser();
                    zzMeetingUser.setId(RandomId.getUUID());

                    zzMeetingUser.setMeetingId(zzMeeting.getId());
                    zzMeetingUser.setCrtUser(userId);
                    zzMeetingUser.setCrtNo(userNo);
                    zzMeetingUser.setCrtName(userName);
                    zzMeetingUser.setCrtHost(userIp);
                    JSONObject userJson = JSONObject.parseObject(userJsonArray.getString(i));
                    zzMeetingUser.setUserId(userJson.getString("userId"));
                    zzMeetingUser.setUserLevel(userJson.getString("userLevels"));
                    zzMeetingUser.setUserName(userJson.getString("userName"));
                    zzMeetingUser.setUserNo(userJson.getString("userNo"));
                    String roleCode = Common.nulToEmptyString(userJson.getString("roleCode"));
                    /*if(!zzCodeMeetingRoleService.ifRoleCodeExist(roleCode)){
                        res.rel(false);
                        res.data("群成员："+userJson.getString("userName")+" 对应的会议角色不存在或已经失效！");
                        return  res;
                    }*/
                    zzMeetingUser.setRoleCode(roleCode);
                    meetUserList.add(zzMeetingUser);
                }
            }

            zzMeeting.setMeetName(Common.nulToEmptyString(jsonObject.getString("groupName")));
            zzMeeting.setMeetDescribe(Common.nulToEmptyString(jsonObject.getString("groupDescribe")));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            zzMeeting.setBeginTime(dateFormat.parse(Common.nulToEmptyString(jsonObject.getString("beginTime"))));
            zzMeeting.setEndTime(dateFormat.parse(Common.nulToEmptyString(jsonObject.getString("endTime"))));
            //注意cross 表示的是会议类型
            zzMeeting.setMeetType(Common.nulToEmptyString(jsonObject.getString("cross")));
            zzMeeting.setPname(Common.nulToEmptyString(jsonObject.getString("pname")));
            zzMeeting.setScop(Common.nulToEmptyString(jsonObject.getString("scop")));
            zzMeeting.setLevels(Common.nulToEmptyString(jsonObject.getString("levels")));
            zzMeeting.setOrg(Common.nulToEmptyString(jsonObject.getString("org")));
            //注意groupType表示的是跨场所
            zzMeeting.setIscross(Common.nulToEmptyString(jsonObject.getString("groupType")));
            //zzMeeting.setAllProgress(Common.nulToEmptyString(jsonObject.getString("allProgress")));
            List<GeneralCodeNameVo> dataList = this.zzMeetingDao.getMeetProgressCodeList();
            //默认有两个议程
            zzMeeting.setAllProgress(MeetingConst.FIRST_MEET_PROGRESS+","+MeetingConst.LAST_MEET_PROGRESS);
            //设置当前议程未选中任何议程
            zzMeeting.setCurrentProgress("0");
            Common.putVoNullStringToEmptyString(zzMeeting);
            /*//添加会议*/
            this.zzMeetingDao.add(zzMeeting);
            Common.putVoNullStringToEmptyString(meetUserList);
            /*//添加成员*/
            this.zzMeetingUserService.addListUsers(meetUserList);

        }catch (Exception e){
            logger.error("创建会议出错");
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
            /*//手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }
    /**查询会议议程代码表*/
    @Override
    public ListRestResponse getMeetProgressCodeList(){
        List<GeneralCodeNameVo> dataList = this.zzMeetingDao.getMeetProgressCodeList();
        String msg = "200";
        return new ListRestResponse(msg,dataList.size(),dataList);
    }
    @Override
    /**会议角色代码表*/
    public ListRestResponse getMeetRoleCodeList(){
        List<MeetRoleCodeVo> datalist = getMeetRoleCodeListData();
        return new ListRestResponse("200",datalist.size(),datalist);
    }
    /**会议角色代码表*/
    public List<MeetRoleCodeVo> getMeetRoleCodeListData(){
        List<Map> datas = this.zzMeetingDao.getMeetRoleCodeList();
        List<MeetRoleCodeVo> res = new ArrayList<>(16);
        String codeKey = "CODE";
        String nameKey = "NAME";
        String functionKey = "MEETFUNCTION";
        for(Map map : datas){
            MeetRoleCodeVo meetRoleCodeVo = new MeetRoleCodeVo();
            meetRoleCodeVo.setCode(Common.nulToEmptyString(map.get(codeKey)));
            meetRoleCodeVo.setName(Common.nulToEmptyString(map.get(nameKey)));
            String functions = Common.nulToEmptyString(map.get(functionKey));
            String newFunctions = "";
            String[] functionArr = functions.split(",");
            for(String function : functionArr){
                newFunctions+=(",'"+function+"'");
            }
            newFunctions = newFunctions.substring(1);
            List<GeneralCodeNameVo> functionsList = this.zzMeetingDao.getMeetFunctionsByCodes(newFunctions);
            meetRoleCodeVo.setFunctionList(functionsList);
            res.add(meetRoleCodeVo);
        }
        return res;
    }
    /**会议功能代码表*/
    @Override
    public ListRestResponse getMeetFunctionCodeList(){
        List<GeneralCodeNameVo> dataList = this.zzMeetingDao.getMeetFunctionCodeList();
        return new ListRestResponse("200",dataList.size(),dataList);
    }

    @Override
//    public ObjectRestResponse changeMeetStatus(String meetingId,String code,String userId,String userName,String userNo,String userIp) {
    public ObjectRestResponse changeMeetAgenda(Map params) {
        String userId = params.get("userId").toString();
        String meetingId = params.get("meetingId").toString();
        //会议议程下标 从1开始
        String code = params.get("code").toString();
        String userName = params.get("userName").toString();
        String userNo = params.get("userNo").toString();
        String userIp = params.get("userIp").toString();
        ZzMeeting meeting  = new ZzMeeting();
        meeting.setUpdUser(userId);
        meeting.setUpdName(userName);
        meeting.setUpdHost(userIp);
        meeting.setId(meetingId);
        meeting.setCurrentProgress(code);
        //会议开始
        boolean meetStart = false;
        String startMeetIndex = "1";
        //第一个议程
        if (startMeetIndex.equals(code)){
            //会议进行中
            // todo 会议开始是否需要绑定socket通道待确定
            meetStart = true;
            meeting.setMeetStatus(MessageType.MEETING_GOING_ON+"");
        }
        //会议结束
        boolean meetOver = false;
        MeetingDto meetingDto = this.getMeetInf(meetingId);
        String allProgress = meetingDto.getAllProgress();
        String[] allProgressStr = allProgress.split(",");
        int progressLeng = allProgressStr.length;
        //最后一个议程
        if(Integer.parseInt(code)==progressLeng){
            //会议结束
            // todo 会议结束是否需要解绑socket通道
            meetOver = true;
            meeting.setMeetStatus(MessageType.CLOSE_MEETING+"");
        }
        this.zzMeetingDao.update(meeting);
        ObjectRestResponse res = this.pushNewMeetInfToSocket(userId,meetingId);
        return res;
    }


    @Override
    public ListRestResponse getMeetingListForContacts(String userId){
        List<MeetingDto> dataList = this.zzMeetingDao.getMeetingListForContacts(userId);
        List <MeetingVo> dataVoList = new ArrayList<>(16);
        try{

            for(int m = 0;m<dataList.size();m++){
                MeetingVo meetingVo = this.queryById(dataList.get(m).getId());
                dataVoList.add(meetingVo);
            }
        }catch (Exception e){
            logger.error("获取联系人会议列表出错！");
            logger.error(Common.getExceptionMessage(e));
        }



        return new ListRestResponse("200",dataVoList.size(),dataVoList);
    }
    @Override
    /**
     * 更改会议议程列表
     */
    public int changeMeetAgendaList(ZzMeeting zzMeeting){
       int i = this.zzMeetingDao.update(zzMeeting);
       this.pushNewMeetInfToSocket(zzMeeting.getUpdUser(),zzMeeting.getId());
       return  i;
    }
   /**
   * @MethodName: pushNewMeetInfToSocket
    * @Description: socket推送消息到前端
    * @Param: [meetId]
    * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
    * @Author: zhuqz
    * @Date: 2019/10/22
   **/
   @Override
    public ObjectRestResponse pushNewMeetInfToSocket(String userId,String meetingId){
        try {
            MeetingVo resMeeting = this.queryById(meetingId);
            SocketMsgDetailVo answerToFrontReponse = new SocketMsgDetailVo();
            answerToFrontReponse.setCode(SocketMsgDetailTypeEnum.MEET_CHANGE);
            answerToFrontReponse.setData(resMeeting);
            // TODO: 2019/10/14 通知会议所有人员JSON.toJSONString(data)
            SocketMsgVo msgVo = new SocketMsgVo();
            msgVo.setCode(SocketMsgTypeEnum.TEAM_MSG);
            msgVo.setReceiver(meetingId);
            msgVo.setMsg(answerToFrontReponse);
            //校验消息
            CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo);
            //只有消息合法才去绑定socket通信频道
            if(cRes.getRes()){
                rabbitMqMsgProducer.sendSocketTeamMsg(msgVo);
            }
        }catch (Exception e){
            logger.error("会议变更推送前端报错！");
            logger.error(Common.getExceptionMessage(e));
            return new ObjectRestResponse().data("操作失败").msg("200").rel(false);
        }
        return new ObjectRestResponse().data("操作成功").msg("200").rel(true);
    }

    /**
     * 处理会议议程
     * @param meetingVo
     * @param allProgress 当前会议所有的议程
     * @param currentProgress 当前议程下标
     */
    private void dealMeetProgress(MeetingVo meetingVo,String allProgress,String currentProgress){


        try {
            MeetingCurrentProgressVo meetingCurrentProgressVo = new MeetingCurrentProgressVo();
            String[] allProgressArr = allProgress.split(",");
            List<GeneralCodeNameVo> allProgressList = new ArrayList<>(16);

            /*List<GeneralCodeNameVo> codeList = this.zzMeetingDao.getMeetProgressCodeList();
            for(int i=0;i<allProgressArr.length;i++){
                GeneralCodeNameVo generalCodeNameVo = new GeneralCodeNameVo();
                generalCodeNameVo.setCode(allProgressArr[i]);
                for(int j = 0;j<codeList.size();j++){
                    if(allProgressArr[i].equals(codeList.get(j).getCode())){
                        generalCodeNameVo.setName(codeList.get(j).getName());
                    }
                }
                allProgressList.add(generalCodeNameVo);
                //如果是当前议程
                if(Integer.parseInt(currentProgress)-1 == i){
                    Common.copyObject(generalCodeNameVo,meetingCurrentProgressVo);
                    meetingCurrentProgressVo.setIndex(currentProgress);
                }
            }*/
            for(int i=0;i<allProgressArr.length;i++){
                GeneralCodeNameVo generalCodeNameVo = new GeneralCodeNameVo();
                generalCodeNameVo.setName(allProgressArr[i]);
                allProgressList.add(generalCodeNameVo);
                //如果是当前议程
                if(Integer.parseInt(currentProgress)-1 == i){
                    Common.copyObject(generalCodeNameVo,meetingCurrentProgressVo);
                }
            }
            meetingCurrentProgressVo.setIndex(currentProgress);
            meetingVo.setAllProgress(allProgressList);
            meetingVo.setCurrentProgress(meetingCurrentProgressVo);
        } catch (Exception e) {
            logger.error("处理会议议程出错！");
            logger.error(Common.getExceptionMessage(e));
        }
    }

    /**
     * 获取会议信息，不包括人员，供内部接口调用
     * @param meetId
     * @return
     */
    @Override
    public MeetingDto getMeetInf(String meetId){
        return  this.zzMeetingDao.queryById(meetId);
    }
}
