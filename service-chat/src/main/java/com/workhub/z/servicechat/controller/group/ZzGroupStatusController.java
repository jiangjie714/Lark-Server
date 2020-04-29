package com.workhub.z.servicechat.controller.group;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.GroupStatusVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.service.ZzGroupStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

/**
 * author:zhuqz
 * description:
 * date:2019/8/26 17:08
 **/
@RestController
@RequestMapping("zzGroupStatus")
public class ZzGroupStatusController {
    @Autowired
    ZzGroupStatusService zzGroupStatusService;
    @Autowired
    private HttpServletRequest request;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    @PostMapping("add")
    public ObjectRestResponse add(@RequestBody ZzGroupStatus zzGroupStatus) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.msg("200");
        res.rel(true);
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperator(userId);
        zzGroupStatus.setOperatorName(userName);
        int i = this.zzGroupStatusService.add(zzGroupStatus);
        return  res;
    }

    /**
    todo:使用
    groupId 群id
    operator 操作人
    groupName 群名称
    operatorName 操作人姓名
    operateType 操作类型
    timeBegin 开始时间
    timeEnd 结束时间
    all 如果有次参数且是1 那么查询全部
    */
    @PostMapping("query")
    public TableResultResponse<GroupStatusVo> query(@RequestParam Map params) throws Exception{
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String all = Common.nulToEmptyString(params.get("all"));//是否是全部查询
        if(!"1".equals(all)){//值查询当前人在群里能看到的记录，而不是全部
            params.put("userId",userId);
        }
        return  this.zzGroupStatusService.query(params);
    }
}
