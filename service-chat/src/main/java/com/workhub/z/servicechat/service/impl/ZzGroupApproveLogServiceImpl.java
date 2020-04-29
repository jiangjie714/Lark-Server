package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.GroupApproveLogVo;
import com.workhub.z.servicechat.VO.GroupMemberVo;
import com.workhub.z.servicechat.VO.MeetUserVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzGroupApproveLogDao;
import com.workhub.z.servicechat.entity.group.ZzGroupApproveLog;
import com.workhub.z.servicechat.service.ZzGroupApproveLogService;
import com.workhub.z.servicechat.service.ZzGroupApproveService;
import com.workhub.z.servicechat.service.ZzMeetingUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:群(会议)审批日志
 * date:2019/9/13 14:23
 **/
@Service("zzGroupApproveLogService")
public class ZzGroupApproveLogServiceImpl implements ZzGroupApproveLogService {
    private static Logger log = LoggerFactory.getLogger(ZzGroupApproveLogServiceImpl.class);
    @Resource
    private ZzGroupApproveLogDao zzGroupApproveLogDao;
    @Resource
    private ZzGroupApproveService zzGroupApproveService;
    @Resource
    private ZzMeetingUserService zzMeetingUserService;
    //新增日志信息
    @Override
    public int add(ZzGroupApproveLog zzGroupApproveLog){
        return this.zzGroupApproveLogDao.add(zzGroupApproveLog);
    }
    //获取日志信息
    @Override
    public  TableResultResponse getApproveLogInf(Map<String,String> params){
        int pageNum=1;
        int pageSize=10;
        try {
            pageNum=Integer.valueOf(Common.nulToEmptyString(params.get("pageNo")));
            pageSize=Integer.valueOf(Common.nulToEmptyString(params.get("pageSize")));
        }catch (Exception e){
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        /**默认是群日志*/
        String typeKey = "type";
        if("".equals(Common.nulToEmptyString(params.get(typeKey)))){
            String groupTypeValue = "0";
            params.put(typeKey,groupTypeValue);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<GroupApproveLogVo> dataList=this.zzGroupApproveLogDao.getApproveLogInf(params);;
        PageInfo pageInfo = new PageInfo<>(dataList);

        try {
            Common.putVoNullStringToEmptyString(dataList);
            List<GroupMemberVo> groupMemberVos = new ArrayList<>();
            //如果是群日志
            if("0".equals(params.get(typeKey))){
                for(GroupApproveLogVo groupApproveLogVo:dataList){
                    String groupId = groupApproveLogVo.getGroupId();
                    String msg = this.zzGroupApproveService.getSingleInfByGroupId(groupId);
                    JSONObject jsonObject = JSONObject.parseObject(msg);
                    String message = jsonObject.getString("data");
                    JSONObject groupJson = JSONObject.parseObject(message);

                    JSONArray userList = JSONObject.parseArray(groupJson.getString("userList"));
                    if(userList!=null){
                        for (int i = 0; i < userList.size(); i++) {
                            GroupMemberVo groupMemberVo = new GroupMemberVo();
                            JSONObject userJson = JSONObject.parseObject(userList.getString(i));
                            groupMemberVo.setUserId(Common.nulToEmptyString(userJson.getString("userId")));
                            groupMemberVo.setUserName(Common.nulToEmptyString(userJson.getString("userName")));
                            groupMemberVo.setUserNo(Common.nulToEmptyString(userJson.getString("userNo")));
                            groupMemberVo.setUserLevel(Common.nulToEmptyString(userJson.getString("userLevels")));
                            groupMemberVo.setUserImg(Common.nulToEmptyString(userJson.getString("img")));
                            groupMemberVos.add(groupMemberVo);
                        }
                    }
                    groupApproveLogVo.setMemberList(groupMemberVos);
                }
            }
            //如果是会议日志
            if("1".equals(params.get(typeKey))){
                for(GroupApproveLogVo groupApproveLogVo:dataList){
                    String meetId = groupApproveLogVo.getGroupId();
                    List<MeetUserVo> meetUserVos =  zzMeetingUserService.queryMeetAllUsersVo(meetId);
                    if(meetUserVos!=null){
                        for (int i = 0; i < meetUserVos.size(); i++) {
                            GroupMemberVo groupMemberVo = new GroupMemberVo();
                            groupMemberVo.setUserId(Common.nulToEmptyString(meetUserVos.get(i).getUserId()));
                            groupMemberVo.setUserName(Common.nulToEmptyString(meetUserVos.get(i).getUserName()));
                            groupMemberVo.setUserNo(Common.nulToEmptyString(meetUserVos.get(i).getUserNo()));
                            groupMemberVo.setUserLevel(Common.nulToEmptyString(meetUserVos.get(i).getUserLevel()));
                            groupMemberVo.setUserImg(Common.nulToEmptyString(meetUserVos.get(i).getUserImg()));
                            groupMemberVos.add(groupMemberVo);
                        }
                    }
                    groupApproveLogVo.setMemberList(groupMemberVos);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
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
