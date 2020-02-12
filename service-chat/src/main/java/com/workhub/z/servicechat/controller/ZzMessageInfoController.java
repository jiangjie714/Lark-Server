package com.workhub.z.servicechat.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.processor.ProcessMsg;
import com.workhub.z.servicechat.service.ZzMessageInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

            String userIp = common.nulToEmptyString(request.getHeader("userHost"));
            String userId = common.nulToEmptyString(request.getHeader("userId"));
            try {
                MsgSendStatusVo msgSendStatusVo = processMsg.process(userId,messageInf,userIp);
                res.data(msgSendStatusVo) ;
            }catch (Exception e){
                log.error("发送消息出错！！！");
                log.error(common.getExceptionMessage(e));
                res.rel(false);
                res.msg("500");
                res.data("发送失败");
            }
            return  res;
    }
}