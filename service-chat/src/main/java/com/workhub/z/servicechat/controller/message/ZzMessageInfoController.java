package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.processor.ProcessMsg;
import com.workhub.z.servicechat.service.ZzMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 消息存储(ZzMessageInfo)表控制层
 *
 * @author makejava
 * @since 2019-06-23 13:50:41
 */
@RestController
@RequestMapping("zzMessageInfo")
public class ZzMessageInfoController {
    private static Logger log = LoggerFactory.getLogger(ZzMessageInfoController.class);
    /**
     * 服务对象
     */
    @Resource
    private ZzMessageInfoService zzMessageInfoService;
    @Autowired
    protected ProcessMsg processMsg;
    @Autowired
    protected HttpServletRequest request;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    /**
     * 消息监控 私聊(改为提供接口，使用admin服务调用)
     *
     * @param params  pageSize条数,pageNum页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
     * @return 列表
     */
    @PostMapping("queryAllMessagePrivate2")
    public TableResultResponse queryAllMessagePrivate2(@RequestBody Map params) throws Exception{
        return this.zzMessageInfoService.queryAllMessagePrivate2(params);
    }
    /**
     * 消息监控 群聊(改为提供接口，使用admin服务调用)
     *
     * @param params  pageSize条数,pageNum页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
     * @return 列表
     */
    @PostMapping("queryAllMessageGroup2")
    public TableResultResponse queryAllMessageGroup2(@RequestBody Map params) throws Exception{
        return this.zzMessageInfoService.queryAllMessageGroup2(params);
    }
    /**
     * 消息监控 私聊(改为提供接口，使用admin服务调用)
     *
     * @param params  pageSize条数,pageNo页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
     * @return 列表
     */
    @PostMapping("queryAllMessagePrivate")
    public TableResultResponse queryAllMessagePrivate(@RequestBody Map params) throws Exception{
        params.put("teamType","USER");
        return this.zzMessageInfoService.queryAllMessageMonitor(params);
    }
    /**
     * 消息监控 群聊(改为提供接口，使用admin服务调用)
     *
     * @param params  pageSize条数,pageNo页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
     * @return 列表
     */
    @PostMapping("queryAllMessageGroup")
    public TableResultResponse queryAllMessageGroup(@RequestBody Map params) throws Exception{
        params.put("teamType","GROUP");
        return this.zzMessageInfoService.queryAllMessageMonitor(params);
    }

    /**
     * 发送消息短连接 该接口来自以前socket前端到后端的通信
     * 目前该功能要求前端发送的消息格式和以往一样
     * @param messageInf
     * @return
     */
    @PostMapping("sendMessage")
    public ObjectRestResponse sendMessage(@RequestBody String messageInf){
            ObjectRestResponse res = new ObjectRestResponse();
            res.rel(true);
            res.msg("200");

            String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
            String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
            try {
                MsgSendStatusVo msgSendStatusVo = processMsg.process(userId,messageInf,userIp);
                res.data(msgSendStatusVo) ;
            }catch (Exception e){
                log.error("发送消息出错！！！");
                log.error(Common.getExceptionMessage(e));
                res.rel(false);
                res.msg("500");
                res.data("发送失败");
            }
            return  res;
    }
    /**
     * @MethodName:
     * @Description: 消息确认
     * @Param:
     * @Return: com.github.hollykunge.security.Common.msg.ObjectRestResponse 返回会议id
     * @Author:
     * @Date: 2020/02/23
     **/
    @PostMapping("msgIdentify")
    public ObjectRestResponse msgIdentify(){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");

        return res;
    }

    /**
     * 消息撤销
     * @param msg msgId 消息id；receiver 接收人；type：类型0私聊1群2会议
     * @return
     */
    @PutMapping("msgCancel")
    public ObjectRestResponse msgCancel(@RequestBody Map msg){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
      int i = this.zzMessageInfoService.msgCancel(Common.nulToEmptyString(msg.get("msgId")),Common.nulToEmptyString(msg.get("receiver")),Common.nulToEmptyString(msg.get("type")),userId);
      if(i==0){
          res.rel(false);
          res.msg("500");
          res.data("消息不存在或者当前用户不是发送人");
          return res;
      }
        return res;
    }

    /**
     * 导出聊天
     * @param receiver 接收人（聊天对象）
     * @param beginDate 开始日期 yyyy-MM-dd
     * @param endDate 开始日期 yyyy-MM-dd
     * @param type user私聊group群meet会议
     * @return
     */
    @GetMapping("exportMsg")
    public void exportMsg(@RequestParam("receiver") String receiver,
                                            @RequestParam(name = "beginDate",required=false) String beginDate,
                                                @RequestParam(name = "endDate",required=false) String endDate,
                                                    @RequestParam("type") String type,
                                                        HttpServletResponse httpServletResponse){
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        this.zzMessageInfoService.exportHistoryMessageForSingle(userId,receiver,beginDate,endDate,type,httpServletResponse);
    }

}