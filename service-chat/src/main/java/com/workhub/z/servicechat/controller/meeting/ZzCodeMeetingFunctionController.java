package com.workhub.z.servicechat.controller.meeting;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.entity.meeting.ZzCodeMeetingFunction;
import com.workhub.z.servicechat.service.ZzCodeMeetingFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author zhuqz
 * description:会议功能菜单
 * date:2019/9/23 14:55
 **/
@RestController
@RequestMapping("zzCodeMeetingFunction")
public class ZzCodeMeetingFunctionController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzCodeMeetingFunctionService zzCodeMeetingFunctionService;
    @Resource
    private HttpServletRequest request;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    /**
     * @MethodName: add
     * @Description: 新增
     * @Param: [zzCodeMeetingFunction]
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse 返回id
     * @Author: zhuqz
     * @Date: 2019/9/20
     **/
    @PostMapping("add")
    public ObjectRestResponse add(@RequestBody ZzCodeMeetingFunction zzCodeMeetingFunction) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            zzCodeMeetingFunction.setCrtUser(userId);
            zzCodeMeetingFunction.setCrtName(userName);
            zzCodeMeetingFunction.setCrtHost(userIp);
            String meetId = this.zzCodeMeetingFunctionService.add(zzCodeMeetingFunction);
            res.data(meetId);
            String duplicateRes = "0";
            if(duplicateRes.equals(meetId)){
                res.data("名称或者代码重复");
                res.rel(false);
            }
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
     * @Param: [ZzCodeMeetingFunction]id,code编码，name名称，isUse是否使用1是0否
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/20
     **/
    @PutMapping("update")
    public  ObjectRestResponse update(@RequestBody ZzCodeMeetingFunction zzCodeMeetingFunction) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userNo = Common.nulToEmptyString(request.getHeader(pidInHeaderRequest));
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        try {
            zzCodeMeetingFunction.setUpdHost(userIp);
            zzCodeMeetingFunction.setUpdName(userName);
            zzCodeMeetingFunction.setUpdUser(userId);
            this.zzCodeMeetingFunctionService.update(zzCodeMeetingFunction);
        }catch (Exception e){
            res.rel(false);
            res.data("系统出错");
            logger.error(Common.getExceptionMessage(e));
        }
        return res;
    }
    /**
    * @MethodName: delete
     * @Description: 删除
     * @Param: [param]code 编码，id主键 两个任意传一个即可
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/23
    **/
    @DeleteMapping("delete")
    public ObjectRestResponse delete(@RequestBody Map param) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        int noExist = 0;
        int noParam = -2;
        try {
            int i = this.zzCodeMeetingFunctionService.deleteData(param);
            if(i==noExist){
                res.rel(false);
                res.data("主键或者编码不存在");
            }else if(i==noParam){
                res.rel(false);
                res.data("请传递要删除的主键或者编码");
            }
        }catch (Exception e){
            res.rel(false);
            res.data("系统出错");
            logger.error(Common.getExceptionMessage(e));
        }
        return res;
    }
    /**
    * @MethodName: query
     * @Description: 查询
     * @Param: [param] pageSize、pageNo页码页数；isUse是否使用1是0否；name名称
     * @Return: com.github.hollykunge.security.Common.msg.TableResultResponse
     * @Author: zhuqz
     * @Date: 2019/9/23
    **/
    @GetMapping("query")
    public TableResultResponse query(@RequestParam Map param) throws Exception{
        return this.zzCodeMeetingFunctionService.query(param);
    }
}
