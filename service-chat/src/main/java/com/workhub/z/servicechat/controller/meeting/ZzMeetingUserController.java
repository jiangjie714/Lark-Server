package com.workhub.z.servicechat.controller.meeting;

import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.MeetingVo;
import com.workhub.z.servicechat.VO.UserCurrentDayMeetJobVo;
import com.workhub.z.servicechat.config.common;
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
/**
* @MethodName: addUser
 * @Description: 添加用户
 * @Param: [zzMeetingUser]meetingId会议id；userId用户id；roleCode用户角色
 * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
 * @Author: zhuqz
 * @Date: 2019/9/20
**/
    @PostMapping("addUser")
    public ObjectRestResponse addUser(@RequestBody ZzMeetingUser zzMeetingUser) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId = common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userNo = common.nulToEmptyString(request.getHeader("pid"));
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
        try {
            zzMeetingUser.setCrtHost(userIp);
            zzMeetingUser.setCrtName(userName);
            zzMeetingUser.setCrtNo(userNo);
            zzMeetingUser.setCrtUser(userId);
            this.zzMeetingUserService.addUser(zzMeetingUser);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
/**
* @MethodName: delUser
 * @Description: 删除用户
 * @Param: [meetId, userId]会议id，人员id
 * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
 * @Author: zhuqz
 * @Date: 2019/9/20
**/
    @DeleteMapping("delUser")
    public ObjectRestResponse delUser(@RequestParam String meetId,@RequestParam String userId) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String operateId = common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userNo = common.nulToEmptyString(request.getHeader("pid"));
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
        try {
            this.zzMeetingUserService.delUser(meetId,userId);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
    /**
    * @MethodName: queryMeetAllUsers
     * @Description: 查询会议所有用户
     * @Param: [meetId]
     * @Return: com.github.hollykunge.security.common.msg.ListRestResponse
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
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/20
    **/
    @PutMapping("updateUser")
    public ObjectRestResponse  updateUser(@RequestBody  ZzMeetingUser meetingUser) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String operateId = common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userNo = common.nulToEmptyString(request.getHeader("pid"));
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
        try {
            meetingUser.setUpdNo(userNo);
            meetingUser.setUpdHost(userIp);
            meetingUser.setUpdUser(operateId);
            meetingUser.setUpdName(userName);
            this.zzMeetingUserService.updateUser(meetingUser);
            res.data("操作成功");
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));
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
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/10/21
    **/
    @PutMapping("updateUserList")
    public ObjectRestResponse  updateUserList(@RequestBody List<ZzMeetingUser> meetingUsers) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String operateId = common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userNo = common.nulToEmptyString(request.getHeader("pid"));
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
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
            logger.error(common.getExceptionMessage(e));
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
        String operateId = common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userNo = common.nulToEmptyString(request.getHeader("pid"));
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
        try {
            int i = this.zzMeetingUserService.editMeetUser(meetingVo,operateId,userName,userNo,userIp);
            if(i!=1){
                res.rel(false);
                res.data("操作出错，会议可能已经不存在");
            }
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));
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
        String userId = common.nulToEmptyString(request.getHeader("userId"));
        List<UserCurrentDayMeetJobVo> data = this.zzMeetingUserService.getUserCurrentDayMeetJob(userId);
        return new ListRestResponse("500",data.size(),data);
    }
}
