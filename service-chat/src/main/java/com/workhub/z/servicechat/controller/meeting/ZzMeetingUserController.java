package com.workhub.z.servicechat.controller.meeting;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.MeetingVo;
import com.workhub.z.servicechat.VO.UserCurrentDayMeetJobVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.entity.meeting.ZzMeetingUser;
import com.workhub.z.servicechat.service.ZzMeetingUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author:zhuqz
 * description:会议用户管理
 * date:2019/9/20 15:17
 **/
@RestController
@RequestMapping("zzMeetingUser")
public class ZzMeetingUserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    ZzMeetingUserService zzMeetingUserService;
    @Autowired
    HttpServletRequest request;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
/**
* @MethodName: addUser
 * @Description: 添加用户
 * @Param: [zzMeetingUser]meetingId会议id；userId用户id；roleCode用户角色
 * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
 * @Author: zhuqz
 * @Date: 2019/9/20
**/
    @PostMapping("addUser")
    public ObjectRestResponse addUser(@RequestBody ZzMeetingUser zzMeetingUser) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            zzMeetingUser.setCrtHost(userIp);
            zzMeetingUser.setCrtName(userName);
            zzMeetingUser.setCrtNo(userNo);
            zzMeetingUser.setCrtUser(userId);
            this.zzMeetingUserService.addUser(zzMeetingUser);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
/**
* @MethodName: delUser
 * @Description: 删除用户
 * @Param: [meetId, userId]会议id，人员id
 * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
 * @Author: zhuqz
 * @Date: 2019/9/20
**/
    @DeleteMapping("delUser")
    public ObjectRestResponse delUser(@RequestParam String meetId,@RequestParam String userId) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String operateId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            this.zzMeetingUserService.delUser(meetId,userId);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
    /**
    * @MethodName: queryMeetAllUsers
     * @Description: 查询会议所有用户
     * @Param: [meetId]
     * @Return: com.github.hollykunge.security.Common.msg.ListRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/20
    **/
    @GetMapping("queryMeetAllUsers")
    public ListRestResponse queryMeetAllUsers(@RequestParam String meetId){
        return  this.zzMeetingUserService.queryMeetAllUsers(meetId);
    }
    /**
     * todo:使用
    * @MethodName: updateUser
     * @Description:
     * @Param: [meetingUser]meetId会议id，userId用户id，roleCode角色编码
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/20
    **/
    @PutMapping("updateUser")
    public ObjectRestResponse  updateUser(@RequestBody  ZzMeetingUser meetingUser) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String operateId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            meetingUser.setUpdNo(userNo);
            meetingUser.setUpdHost(userIp);
            meetingUser.setUpdUser(operateId);
            meetingUser.setUpdName(userName);
            this.zzMeetingUserService.updateUser(meetingUser);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
    /**
     * todo:使用
    * @MethodName: updateUserList
     * @Description: 更新用户列表信息(批量)
     * @Param: [meetingUser]
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/10/21
    **/
    @PutMapping("updateUserList")
    public ObjectRestResponse  updateUserList(@RequestBody List<ZzMeetingUser> meetingUsers) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String operateId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            for(ZzMeetingUser meetingUser : meetingUsers){
                meetingUser.setUpdNo(userNo);
                meetingUser.setUpdHost(userIp);
                meetingUser.setUpdUser(operateId);
                meetingUser.setUpdName(userName);
            }
            this.zzMeetingUserService.updateUserList(meetingUsers);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }

    /**
     * 会议用户编辑
     * @param meetingVo
     * @return
     * @throws Exception
     */
    @PutMapping("editMeetUser")
    public ObjectRestResponse  editMeetUser(@RequestBody MeetingVo meetingVo) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        String operateId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        //只有会议人员可以邀请其他人员
        List<String> memberList = zzMeetingUserService.getMeetingByUserId(meetingVo.getId());
        if(!memberList.contains(operateId)){
            res.rel(false);
            res.data("操作失败，操作人不在会议内");
        }
        try {
            int i = this.zzMeetingUserService.editMeetUser(meetingVo,operateId,userName,userNo,userIp);
            if(i!=1){
                res.rel(false);
                res.data("操作出错，会议可能已经不存在");
            }
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }

    /**
     * 获取用户当天需要提醒的会议列表
     * @return
     */
    @GetMapping("getUserCurrentDayMeetJob")
    public ListRestResponse getUserCurrentDayMeetJob(){
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        List<UserCurrentDayMeetJobVo> data = this.zzMeetingUserService.getUserCurrentDayMeetJob(userId);
        return new ListRestResponse("500",data.size(),data);
    }
}
