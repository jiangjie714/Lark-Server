package com.workhub.z.servicechat.controller.group;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.entity.group.ZzGroupApproveLog;
import com.workhub.z.servicechat.service.ZzGroupApproveLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

/**
 * author:zhuqz
 * description:群审批日志
 * date:2019/9/13 14:22
 **/
@RestController
@RequestMapping("/zzGroupApproveLog")
public class ZzGroupApproveLogController {
    private static Logger log = LoggerFactory.getLogger(ZzGroupApproveController.class);
    @Autowired
    private HttpServletRequest request;
    @Resource
    ZzGroupApproveLogService zzGroupApproveLogService;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    //新增接口
    @PostMapping("/add")
    public ObjectRestResponse add(@RequestBody ZzGroupApproveLog zzGroupApproveLog) throws Exception{
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.rel(true);
        objectRestResponse.msg("200");
        objectRestResponse.data("操作成功");
        this.zzGroupApproveLogService.add(zzGroupApproveLog);
        return objectRestResponse;
    }
    //日志信息查询 todo:使用
    //pageSize、pageNo、groupName、operatorName操作人（建群人）、
    //approveName审批人姓名、operateTimeEnd/operateTimeEnd操作时间（建群开始结束时间）
    //type 0群日志1会议日志
    @GetMapping("/getApproveLogInf")
    public TableResultResponse getApproveLogInf(@RequestParam Map params) throws Exception{
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        return this.zzGroupApproveLogService.getApproveLogInf(params);
    }
}
