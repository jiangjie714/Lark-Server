package com.workhub.z.servicechat.controller.meeting;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.MeetingVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.meeting.ZzMeeting;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzMeetingService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议控制器
 * date:2019/9/20 11:02
 **/
@RestController
@RequestMapping("zzMeeting")
public class ZzMeetingController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzMeetingService zzMeetingService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private AdminUserService iUserService;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
/**
* @MethodName: add
 * @Description: 新增会议
 * @Param: [zzMeeting]
 * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse 返回会议id
 * @Author: zhuqz
 * @Date: 2019/9/20
**/
    @PostMapping("add")
    public ObjectRestResponse add(@RequestBody ZzMeeting zzMeeting) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            zzMeeting.setCrtUser(userId);
            zzMeeting.setCrtName(userName);
            zzMeeting.setCrtHost(userIp);
            String meetId = this.zzMeetingService.add(zzMeeting);
            res.data(meetId);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
/**
 * todo:使用
* @MethodName: getSingleMeetingInfo
 * @Description: 查询单个会议的详细信息
 * @Param: [meetId:会议id]
 * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
 * @Author: zhuqz
 * @Date: 2019/9/20
**/
    @GetMapping("getSingleMeetingInfo")
    public ObjectRestResponse getSingleMeetingInfo(@RequestParam String meetId){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");

        try {
            MeetingVo meetingVo  = this.zzMeetingService.queryById(meetId);
            res.data(meetingVo);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
    /**
    * @MethodName: update
     * @Description: 修改
     * @Param: [zzMeeting]
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/20
    **/
    @PutMapping("update")
    public  ObjectRestResponse update(@RequestBody ZzMeeting zzMeeting) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            zzMeeting.setUpdHost(userIp);
            zzMeeting.setUpdName(userName);
            zzMeeting.setUpdUser(userId);
            this.zzMeetingService.update(zzMeeting);
        }catch (Exception e){
            res.rel(false);
            res.data("系统出错");
            logger.error(Common.getExceptionMessage(e));
        }
        return res;
    }
    @DeleteMapping("delete")
    public ObjectRestResponse delete(@RequestParam String meetId) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        try {
            this.zzMeetingService.delete(meetId);
        }catch (Exception e){
            res.rel(false);
            res.data("系统出错");
            logger.error(Common.getExceptionMessage(e));
        }
        return res;
    }
    /**
    * @MethodName: createMeeting
     * @Description: 创建会议
     * @Param: [meetingJson] 会议json
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse 返回会议id
     * @Author: zhuqz
     * @Date: 2019/9/24
    **/
    @PostMapping("createMeeting")
    public ObjectRestResponse createMeeting(@RequestBody String meetingJson) throws Exception{

        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        String userNo= Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        //会议id
        String groupId = RandomId.getUUID();
        JSONObject jsonObject = JSONObject.parseObject(meetingJson);
        String message = jsonObject.getString("data");
        JSONObject groupJson = JSONObject.parseObject(message);
        groupJson.put("groupId",groupId);
        groupJson.put("creatorIp",userIp);
        groupJson.put("creatorNo",userNo);
        jsonObject.put("data",groupJson);
        meetingJson = groupJson.toJSONString();
        ObjectRestResponse res = this.zzMeetingService.createMeeting(meetingJson);
        return  res;
    }
    /**
     * todo:使用
    * @MethodName: getMeetProgressCodeList
     * @Description: 查询会议议程全部代码表（该接口暂时废弃不用了）
     * @Param: []
     * @Return: com.github.hollykunge.security.Common.msg.ListRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/25
    **/
    @GetMapping("getMeetProgressCodeList")
    public ListRestResponse getMeetProgressCodeList(){
        return this.zzMeetingService.getMeetProgressCodeList();
    }
    /**
     * todo:使用
    * @MethodName: getMeetRoleCodeList
     * @Description: 获取会议角色代码表
     * @Param: []
     * @Return: java.util.List<com.workhub.z.servicechat.VO.MeetRoleCodeVo>
     * @Author: zhuqz
     * @Date: 2019/9/26
    **/
    @GetMapping("getMeetRoleCodeList")
    public ListRestResponse getMeetRoleCodeList(){
        return this.zzMeetingService.getMeetRoleCodeList();
    }
    /**
     * @MethodName: getMeetRoleCodeList
     * @Description: 获取会议功能代码表
     * @Param: []
     * @Return: java.util.List<com.workhub.z.servicechat.VO.MeetRoleCodeVo>
     * @Author: zhuqz
     * @Date: 2019/9/26
     **/
    @GetMapping("getMeetFunctionCodeList")
    public ListRestResponse getMeetFunctionCodeList(){
        return this.zzMeetingService.getMeetFunctionCodeList();
    }

    /**
     * todo:使用
    *@Description: 会议议程变更
    *@Param: meetid,code
    *@return:
    *@Author: 忠
    *@date: 2019/10/14
    */
    @PutMapping("changeMeetAgenda")
    public ObjectRestResponse changeMeetAgenda(@RequestBody Map params) throws UnsupportedEncodingException {
        params.put("userId", Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest)));
        params.put("userName",URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8"));
        params.put("userNo", Common.nulToEmptyString(request.getHeader(pidInHeaderRequest)));
        params.put("userIp", Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest)));
        ObjectRestResponse objectRestResponse = this.zzMeetingService.changeMeetAgenda(params);
        return objectRestResponse;
    }

    /**
     * todo:使用
     * 会议议程变列表变更
     * @param zzMeeting id会议id，allProgress 议程列表编码 逗号分割例如：5001,5002,5003
     * @return
     * @throws UnsupportedEncodingException
     */
    @PutMapping("changeMeetAgendaList")
    public ObjectRestResponse changeMeetAgendaList(@RequestBody ZzMeeting zzMeeting) throws UnsupportedEncodingException {
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.setRel(true);
        objectRestResponse.setMessage("200");
        objectRestResponse.setResult("操作成功");
        zzMeeting.setUpdUser(Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest)));
        zzMeeting.setUpdName(URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8"));
        zzMeeting.setUpdHost(Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest)));
        try {
            int i= this.zzMeetingService.changeMeetAgendaList(zzMeeting);
        } catch (Exception e) {
            logger.error("更改会议议程列表出错");
            logger.error(Common.getExceptionMessage(e));
            objectRestResponse.rel(false);
            objectRestResponse.setResult("操作失败");
        }
        return objectRestResponse;
    }

    /**
     * todo:使用
    *@Description: 获取会议列表（联系人）
    *@Param: userid
    *@return: meetingvo
    *@Author: 忠
    *@date: 2019/10/15
    */
    @GetMapping("getMeetingListForContacts")
    public ListRestResponse getMeetingListForContacts(@Param("userId") String userId){
       return this.zzMeetingService.getMeetingListForContacts(userId);
    }

/*    *//**
     * 获取岗位人员列表
     * @param id 岗位id
     * @param secretLevel 密级
     * @return
     *//*
    @GetMapping("getPositionUserList")
    public ListRestResponse getPositionUserList(@RequestParam("id") String id,@RequestParam("secretLevel")String secretLevel){
        List<UserInfo> list = this.iUserService.getUserListBySecret(id,secretLevel);
        return new ListRestResponse("200",list.size(),list);
    }*/
}

