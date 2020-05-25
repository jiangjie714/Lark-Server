package com.github.hollykunge.security.simulation.controller;

import com.alibaba.fastjson.JSON;
import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.simulation.biz.RunBiz;
import com.github.hollykunge.security.simulation.biz.SystemBiz;
import com.zeroc.Ice.ConnectionRefusedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.github.hollykunge.security.simulation.config.Constant.*;

/**
 * @author jihang
 */

@RestController
@RequestMapping("/runControl")
public class RunController {

    @Resource
    private SystemBiz systemBiz;

    @Resource
    private RunBiz runBiz;

    /**
     * 公共返回
     */
    private ObjectRestResponse getObjectRestResponse(String systemId, String ret, String newState) {
        try {
            Map<String, String> ret_j = (Map<String, String>) JSON.parse(ret);
            String retState = ret_j.get("success");
            String retMsg = ret_j.get("message");

            String gbk = new String(retMsg.getBytes("UTF-8"), "gb2312");
            switch (retState) {
                case RT_SUCCESS:
                    systemBiz.updateState(systemId, newState);
                    return new ObjectRestResponse().rel(true).msg(retMsg);
                case RT_EXCEPTION:
                    return new ObjectRestResponse().rel(false).msg("动态库错误，请结束仿真，重新准备");
                default:
                    return new ObjectRestResponse().rel(false).msg(retMsg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真，重新准备");
        }
    }

    /**
     * 系统准备
     */
    @Decrypt
    @RequestMapping(value = "/ready", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemReady(
            @RequestParam("systemId") String systemId, @RequestParam("users") String users,
            @RequestParam("startTime") double startTime, @RequestParam("stopTime") double stopTime,
            @RequestParam("step") double step) {
        try {
            if(!runBiz.hasConfig(systemId)) {
                return new ObjectRestResponse().rel(false).msg("未找到任务配置文件，请生成后重试");
            }
            if (systemId == null || systemId.equals("")
                    || users == null || users.equals("")
                    || startTime < 0.0 || step < 0.0
                    || stopTime < 0.0 || stopTime < startTime) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数有误，请修改后重试");
            }
            String curState = systemBiz.queryState(systemId);
            if (!UNREADY.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("任务状态错误，请结束仿真并重新准备");
            }
            String ret = runBiz.dllSystemPrepare(systemId, startTime, step, stopTime, users);
            return getObjectRestResponse(systemId, ret, PREPARING);
        } catch (ConnectionRefusedException e) {
            System.out.println("systemReady Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("引擎服务不可用，请联系管理员");
        } catch (Exception e) {
            System.out.println("systemReady Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("后台执行错误，请结束仿真后重试");
        }
    }

    /**
     * 系统开始
     */
    @Decrypt
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemStart(@RequestParam("systemId") String systemId) {
        try {
            if(!runBiz.hasConfig(systemId)) {
                return new ObjectRestResponse().rel(false).msg("未找到任务配置文件，请生成后重试");
            }
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String curState = systemBiz.queryState(systemId);
            String ret = null;
            if (PREPARING.equals(curState)) {
                ret = runBiz.dllSystemStart(systemId);
            } else if (PAUSING.equals(curState)) {
                ret = runBiz.dllSystemPause(systemId);
            } else {
                return new ObjectRestResponse().rel(false).msg("任务状态错误，请结束仿真");
            }
            return getObjectRestResponse(systemId, ret, RUNNING);
        } catch (ConnectionRefusedException e) {
            System.out.println("systemStart Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("引擎服务不可用，请联系管理员");
        } catch (Exception e) {
            System.out.println("systemStart Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("后台执行错误，请结束仿真");
        }
    }

    /**
     * 系统暂停及继续
     */
    @Decrypt
    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemPause(@RequestParam("systemId") String systemId) {
        try {
            if(!runBiz.hasConfig(systemId)) {
                return new ObjectRestResponse().rel(false).msg("未找到任务配置文件，请生成后重试");
            }
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String curState = systemBiz.queryState(systemId);
            if (!RUNNING.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("任务状态错误，请结束仿真");
            }
            String ret = runBiz.dllSystemPause(systemId);
            return getObjectRestResponse(systemId, ret, PAUSING);
        } catch (ConnectionRefusedException e) {
            System.out.println("systemPause Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("引擎服务不可用，请联系管理员");
        } catch (Exception e) {
            System.out.println("systemPause Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("后台执行错误，请结束仿真");
        }
    }

    /**
     * 系统结束
     */
    @Decrypt
    @RequestMapping(value = "/end", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemEnd(@RequestParam("systemId") String systemId) {
        try {
            if(!runBiz.hasConfig(systemId)) {
                return new ObjectRestResponse().rel(false).msg("未找到任务配置文件，请生成后重试");
            }
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String ret = runBiz.dllSystemStop(systemId);
            systemBiz.updateState(systemId, UNREADY);
            return getObjectRestResponse(systemId, ret, UNREADY);
        } catch (ConnectionRefusedException e) {
            System.out.println("systemEnd Controller : " + e.getMessage());
            systemBiz.updateState(systemId, UNREADY);
            return new ObjectRestResponse().rel(false).msg("引擎服务不可用，请联系管理员");
        } catch (Exception e) {
            System.out.println("systemEnd Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }

    @Decrypt
    @RequestMapping(value = "/getOnlineNodes", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse getOnlineNodes(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String ret = runBiz.dllGetOnlineNodes(systemId);
            Map<String, String> ret_j = (Map<String, String>) JSON.parse(ret);
            String retState = ret_j.get("success");
            String retMsg = ret_j.get("message");
            switch (retState) {
                case RT_SUCCESS:
                    return new ObjectRestResponse().rel(true).msg(retMsg);
                case RT_EXCEPTION:
                    return new ObjectRestResponse().rel(false).msg("引擎内部错误，请结束仿真");
                default:
                    return new ObjectRestResponse().rel(false).msg(retMsg);
            }
        } catch (ConnectionRefusedException e) {
            System.out.println("getOnlineNodes Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("引擎服务不可用，请联系管理员");
        } catch (Exception e) {
            System.out.println("getOnlineNodes Controller : " + e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }
}
