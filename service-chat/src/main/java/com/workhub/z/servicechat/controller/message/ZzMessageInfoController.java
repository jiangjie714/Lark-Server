package com.workhub.z.servicechat.controller.message;

import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.MessageHistoryVo;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.processor.ProcessMsg;
import com.workhub.z.servicechat.service.ZzMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
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
    @Decrypt
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
     **//*
    @PostMapping("msgIdentify")
    public ObjectRestResponse msgIdentify(){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");

        return res;
    }
*/
    /**
     * 消息撤销
     * @param msgId 消息id；
     * @return
     */
    @Decrypt
    @PutMapping("msgCancel")
    public ObjectRestResponse msgCancel(@RequestParam String msgId){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
      int i = this.zzMessageInfoService.msgCancel(Common.nulToEmptyString(msgId),userId);
      if(i==0){
          res.rel(false);
          res.msg("500");
          res.data("消息不存在或者当前用户不是发送人");
          return res;
      }else if(i==2){
          res.rel(false);
          res.msg("500");
          res.data("超过三分钟不能撤销");
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

    /**
     *  打开了消息面板
     * @param msg

     * @return
     */
    @Decrypt
    @PostMapping("openMsgBoard")
    public ObjectRestResponse openMsgBoard(@RequestBody String msg) throws Exception {
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userIp = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        String sender = (String) Common.getJsonStringKeyValue(msg,"sender");
        String senderName = (String) Common.getJsonStringKeyValue(msg,"senderName");
        String receiver = (String) Common.getJsonStringKeyValue(msg,"receiver");
        String receiverName = (String) Common.getJsonStringKeyValue(msg,"receiverName");
        this.zzMessageInfoService.openMsgBoard(sender,senderName,receiver,receiverName,userIp);
        return  res;
    }

    /**
     *历史消息
     * @param user 用户id
     * @param contact 联系人id
     * @param lastMsgId 最后一条消息id
     * @param type 联系人类型 user、group、meet
     * @param size 每次取几条
     * @return
     */
    @GetMapping("listHistoryMsgs")
    public ListRestResponse listHistoryMsg(
                                           @RequestParam(name = "user") String user,
                                           @RequestParam(name ="contact") String contact,
                                           @RequestParam(name ="lastMsgId",required = false) String lastMsgId,
                                           @RequestParam(name ="type",required = false,defaultValue = "user") String type,
                                           @RequestParam(name ="size",required = false,defaultValue = "50") String size){

        List<String> dataList = zzMessageInfoService.listHistoryMsg(user,contact,lastMsgId,type,size);
        return new ListRestResponse("200",dataList.size(),dataList);
    }
    /**
     *历史消息(非json)
     * @param user 用户id
     * @param contact 联系人id
     * @param lastMsgId 最后一条消息id
     * @param type 联系人类型 user、group、meet
     * @param size 每次取几条
     * @return
     */
    @GetMapping("listHistoryMsgsInf")
    public ListRestResponse listHistoryMsgInf(
            @RequestParam(name = "user") String user,
            @RequestParam(name ="contact") String contact,
            @RequestParam(name ="lastMsgId",required = false) String lastMsgId,
            @RequestParam(name ="type",required = false,defaultValue = "user") String type,
            @RequestParam(name ="size",required = false,defaultValue = "50") String size){

        List<MessageHistoryVo> dataList = zzMessageInfoService.listHistoryMsgInf(user,contact,lastMsgId,type,size);
        return new ListRestResponse("200",dataList.size(),dataList);
    }

    /**
     * 消息监控
     * @param messageLevel 消息密级
     * @param messageContent 消息内容
     * @param senderName 发送人
     * @param receiverName 接收人
     * @param teamType user、group、meet
     * @param timeBegin yyyy-mm-dd
     * @param timeEnd yyyy-mm-dd
     * @param pageSize
     * @param pageNo
     * @return
     * @throws Exception
     */
    @GetMapping("listMonitorMsgs")
    public TableResultResponse listMonitorMsgs (
            @RequestParam(name = "messageLevel",required = false) String messageLevel,
            @RequestParam(name ="messageContent",required = false) String messageContent,
            @RequestParam(name ="senderName",required = false) String senderName,
            @RequestParam(name ="receiverName",required = false) String receiverName,
            @RequestParam(name ="teamType",required = false) String teamType,
            @RequestParam(name ="timeBegin",required = false) String timeBegin,
            @RequestParam(name ="timeEnd",required = false) String timeEnd,
            @RequestParam(name ="pageSize",required = false,defaultValue = "10") String pageSize,
            @RequestParam(name ="pageNo",required = false,defaultValue = "1") String pageNo
            ) throws Exception
    {
        Map<String,String> params = new HashMap<>();
        params.put("messageLevel",Common.nulToEmptyString(messageLevel));
        params.put("messageContent",Common.nulToEmptyString(messageContent));
        params.put("senderName",Common.nulToEmptyString(senderName));
        params.put("receiverName",Common.nulToEmptyString(receiverName));
        params.put("teamType",Common.nulToEmptyString(teamType).toUpperCase());
        params.put("timeBegin",Common.nulToEmptyString(timeBegin));
        params.put("timeEnd",Common.nulToEmptyString(timeEnd));
        params.put("pageSize",Common.nulToEmptyString(pageSize));
        params.put("pageNo",Common.nulToEmptyString(pageNo));
        return this.zzMessageInfoService.queryAllMessageMonitor(params);
    }
}