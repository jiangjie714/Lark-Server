package com.workhub.z.servicechat.controller.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.ChatAdminUserVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.group.ZzGroupApprove;
import com.workhub.z.servicechat.entity.group.ZzGroupApproveLog;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzGroupApproveService;
import com.workhub.z.servicechat.service.ZzRequireApproveAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:zhuqz
 * description:群（会议或者其他资源）审批功能
 * date:2019/9/11 15:56
 **/
@RestController
@RequestMapping("/zzGroupApprove")
public class ZzGroupApproveController {
    private static Logger log = LoggerFactory.getLogger(ZzGroupApproveController.class);
    @Autowired
    private HttpServletRequest request;
    @Resource
    private ZzGroupApproveService zzGroupApproveService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    @Resource
    ZzRequireApproveAuthorityService zzRequireApproveAuthorityService;
    @Resource
    AdminUserService iUserService;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    /**
     * todo:使用
    * @MethodName: add
     * @Description: 新增审批信息
     * @Param: [msg]msg：前台传过来的json串
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/11
     * msg里sourceType这个参数没有用了（如果有的话），groupType标识的是跨场所，cross表示的是会议类型（如果有的话）
    **/
   @PostMapping("/add")
   public ObjectRestResponse add(@RequestBody String msg) throws Exception{
       //log.info("======================="+msg.length()+"");
       String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
       //userId= "123123123123";
       String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
       //userName="测试";
       String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
       String userNo= Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
       ObjectRestResponse objectRestResponse = new ObjectRestResponse();
       objectRestResponse.rel(true);
       objectRestResponse.msg("200");
       objectRestResponse.data("操作成功");
       //群（资源）id
       String groupId = RandomId.getUUID();

       JSONObject jsonObject = JSONObject.parseObject(msg);
       String code = jsonObject.getString("code");
       String message = jsonObject.getString("data");
       JSONObject groupJson = JSONObject.parseObject(message);
       //0群1会议
       String type = "";
       //会议
       if("901".equals(code)){
           type = MessageType.FLOW_LOG_MEET;
       }
       //群
       if("3".equals(code)){
           type = MessageType.FLOW_LOG_GROUP;
       }
       //是否需要审批
       boolean needApproveFlg = true;
       String groupLevel = groupJson.getString("levels");//密级
       //如果是非密的研讨组
       if(MessageType.NO_SECRECT_LEVEL.equals(groupLevel) && MessageType.FLOW_LOG_GROUP.equals(type)){
           //如果是非密不需要审批
           needApproveFlg = false;
       }

       String approveList = groupJson.getString("approveList");
       if(approveList == null){
           approveList = "";
       }
       groupJson.put("groupId",groupId);
       groupJson.put("creatorIp",userIp);
       groupJson.put("creatorNo",userNo);
       jsonObject.put("data",groupJson);
       msg = jsonObject.toJSONString();

       //记录流水日志
       ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
       zzGroupStatus.setId(RandomId.getUUID());
       zzGroupStatus.setOperatorName(Common.nulToEmptyString(userName));
       zzGroupStatus.setOperator(Common.nulToEmptyString(userId));
       //创建群
       zzGroupStatus.setOperateType(MessageType.FLOW_NEW);
       zzGroupStatus.setGroupId(groupId);
       zzGroupStatus.setOperateTime(new Date());
       zzGroupStatus.setType(type);
       /**群成员id*/
       String memberIds = "";
       /*//群成员名称*/
       String memberNames = "";
       JSONArray memberJsonArray = JSONObject.parseArray(groupJson.getString("userList"));
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
       String desPart2 = "";
       if(type.equals(MessageType.FLOW_LOG_GROUP)){
           desPart2 =  "创建了群："+ groupJson.getString("groupName")+"；群成员："+memberNames+"；群成员id："+memberIds+"；操作结果：创建成功";
       }else if(type.equals(MessageType.FLOW_LOG_MEET)){
           desPart2 =  "创建了会议："+ groupJson.getString("groupName")+"；会议成员："+memberNames+"；会议成员id："+memberIds+"；操作结果：创建成功";
       }
       zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+ desPart2);
       //记录审批日志
       ZzGroupApproveLog approveLog = new ZzGroupApproveLog();
       approveLog.setId(RandomId.getUUID());
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
           approveIds = userId;
           approveNames = userName;
       }

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
       approveLog.setOperateType(MessageType.FLOW_NEW);
       approveLog.setOperator(Common.nulToEmptyString(groupJson.getString("creator")));
       approveLog.setOperatorNo(userNo);
       approveLog.setOperatorName(userName);
       approveLog.setOperateTime(new Date());
       approveLog.setType(type);
       String id = RandomId.getUUID();
       try {
           ZzGroupApprove zzGroupApprove  = new ZzGroupApprove();
           zzGroupApprove.setMsg(msg);
           zzGroupApprove.setId(id);
           zzGroupApprove.setGroupId(groupId);
           zzGroupApprove.setApproveList(approveList);
           zzGroupApprove.setCreator(userId);
           zzGroupApprove.setCreatorName(userName);
           zzGroupApprove.setGroupName(groupJson.getString("groupName"));
           zzGroupApprove.setType(type);
           this.zzGroupApproveService.add(zzGroupApprove);
       }catch (Exception e){
           log.error("新建群（会议或其他资源）报错："+ Common.getExceptionMessage(e));
           objectRestResponse.rel(false);
           objectRestResponse.data("操作失败");
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
       ChatAdminUserVo userInfo = iUserService.getUserInfo(userId);
       int require_approve_authority = zzRequireApproveAuthorityService.needApprove(userInfo.getOrgCode());
       //无需审批(1特殊权限2非密的研讨群不用审批)，直接审批通过，生成会议或者群组。注:该功能与本类approve的逻辑保持一致。
       if(((MessageType.NO_REQUIRE_APPROVE_AUTHORITY == require_approve_authority) || !needApproveFlg )&& objectRestResponse.isRel()){
           Map<String,String> approveParam = new HashMap<>(16);
           //数据id
           approveParam.put("id",id);
           //审批通过
           approveParam.put("approveFlg","1");
           approveParam.put("userId",userId);
           approveParam.put("userName",userName);
           approveParam.put("ip","");
           Map<String,String> approveResMap = this.zzGroupApproveService.approve(approveParam);
           //mq消息
           sendApproveLog(approveResMap, userName,userId);
       }
       //记录群状态变动end
       return objectRestResponse;
   }

   /**
    * todo:使用
   * @MethodName: approve
    * @Description: 审批
    * @Param: [param] id：数据主键；approveFlg：审批标记1 通过2 不通过
    * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
    * @Author: zhuqz
    * @Date: 2019/9/11
   **/
    @PutMapping("/approve")
    public ObjectRestResponse approve(@RequestParam Map param) throws Exception{
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        param.put("userId",userId);
        //param.put("userId","yanzhenqing");
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        param.put("userName",userName);
        //param.put("userName","严振卿");
        String ip = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        param.put("ip",ip);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.rel(true);
        objectRestResponse.msg("200");
        objectRestResponse.data("操作成功");

        try {
           Map<String,String> resMap = this.zzGroupApproveService.approve(param);
           //mq消息
            sendApproveLog(resMap,userName,userId);
            if(resMap.get("res").equals("0")){
                objectRestResponse.rel(false);
                objectRestResponse.data("该记录已经审批");
            }
        }catch (Exception e){
            log.error(Common.getExceptionMessage(e));
            objectRestResponse.rel(false);
            objectRestResponse.data("操作失败");
        }
        return objectRestResponse;
    }

    /**
     * 发送审批消息，记录到日志表
     * @param resMap
     * @param userName
     * @param userId
     */
    public void sendApproveLog(Map<String,String> resMap,String userName,String userId){
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
     * todo:使用
    * @MethodName: getApproveList
     * @Description: 查询审批列表
     * @Param: [param] approveFlg：0未审批1通过2不通过3已审批4全部;pageSize;pageNo;type 0 群，1会议
     * groupName 群（会议）名称
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/11
    **/
    @GetMapping("/getApproveList")
    public TableResultResponse getApproveList(@RequestParam Map param) throws Exception{
        String approveFlg = Common.nulToEmptyString(param.get("approveFlg"));
        if(approveFlg.equals("")){
            param.put("approveFlg","0");
        }
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        param.put("userId",userId);
        //param.put("userId","7803632385204eb6ab88265c04dba81f");
        try {
            return this.zzGroupApproveService.getApproveList(param);
        }catch (Exception e){
            log.error("获取审批群列表出错"+ Common.getExceptionMessage(e));
        }
        return null;
    }
    //获取审批信息详细 todo:使用
    @GetMapping("/getApproveGroupDetail")
    public ObjectRestResponse getApproveGroupDetail(@RequestParam String id) throws Exception{
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.rel(true);
        objectRestResponse.msg("200");
        String res =  this.zzGroupApproveService.getApproveGroupDetail(id);
        if(res == null || "".equals(res)){
            objectRestResponse.rel(false);
            objectRestResponse.data("查询出错");
            return  objectRestResponse;
        }

        objectRestResponse.data(res);
        return  objectRestResponse;
    }
    /**
     * todo:使用
     * @MethodName: getApplyGroupList
     * @Description: 查询申请列表
     * @Param: [param] approveFlg：0未审批1通过2不通过3已审批4全部;pageSize;pageNo;type 0 群，1会议,-1全部
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/11
     **/
    @GetMapping("/getApplyGroupList")
    public TableResultResponse getApplyGroupList(@RequestParam Map param) throws Exception{
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");

        String approveFlg = Common.nulToEmptyString(param.get("approveFlg"));
        if(approveFlg.equals("")){
            param.put("approveFlg","0");
        }
        param.put("userId",userId);
        param.put("userName",userName);
        //param.put("userId","7803632385204eb6ab88265c04dba81f");
        try {
            return this.zzGroupApproveService.getApplyGroupList(param);
        }catch (Exception e){
            log.error("获取群申请列表出错"+ Common.getExceptionMessage(e));
        }
        return null;
    }
}
