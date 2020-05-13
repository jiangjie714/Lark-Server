package com.workhub.z.servicechat.controller.group;

import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.config.ResConst;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.ZzGroupApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
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
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    /**
     * @MethodName: add
     * @Description: 新增审批信息
     * @Param: [msg]msg：前台传过来的json串
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/11
     * msg里sourceType这个参数没有用了（如果有的话），groupType标识的是跨场所，cross表示的是会议类型（如果有的话）
    **/
    @Decrypt
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
       int i = this.zzGroupApproveService.saveApprove(msg,userId,userName,userNo,userIp);
       if(i!= ResConst.INT_RES_SUCCESS){
           objectRestResponse.rel(false);
           objectRestResponse.msg("500");
           objectRestResponse.data("操作失败");
       }

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
    @Decrypt
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
