package com.github.hollykunge.security.simulation.controller;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.simulation.biz.DllInvoker;
import com.github.hollykunge.security.simulation.biz.SystemBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.github.hollykunge.security.simulation.config.Constant.*;
import static com.github.hollykunge.security.simulation.config.Constant.RT_EXCEPTION;

/**
 * @author jihang
 */

@RestController
@RequestMapping("/runControl")
public class RunController extends BaseController<SystemBiz, SystemInfo> {

    /**
     * 公共返回
     */
    private ObjectRestResponse getObjectRestResponse(String systemId, String ret, String newState) {
        Map<String, String> ret_j = (Map<String, String>) JSON.parse(ret);
        String retState = ret_j.get("success");
        String retMsg = ret_j.get("message");
        switch (retState) {
            case RT_SUCCESS:
                baseBiz.updateState(systemId, newState);
                return new ObjectRestResponse().rel(true).msg(retMsg);
            case RT_EXCEPTION:
                return new ObjectRestResponse().rel(false).msg("动态库错误，请结束仿真，重新准备");
            default:
                return new ObjectRestResponse().rel(false).msg(retMsg);
        }
    }

    /**
     * 系统准备
     */
    @RequestMapping(value = "/ready", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemReady(
            @RequestParam("systemId") String systemId, @RequestParam("users") String users,
            @RequestParam("startTime") double startTime, @RequestParam("stopTime") double stopTime,
            @RequestParam("step") double step) {
        try {
            if (systemId == null || systemId.equals("")
                    || users == null || users.equals("")
                    || startTime < 0.0 || step < 0.0
                    || stopTime < 0.0 || stopTime < startTime) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真，重新准备");
            }
            String curState = baseBiz.queryState(systemId);
            if (!UNREADY.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("后台状态错误，请结束仿真，重新准备");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllSystemPrepare(systemId, startTime, step, users);
            return getObjectRestResponse(systemId, ret, PREPARING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真，重新准备");
        }
    }

    /**
     * 系统开始
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemStart(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            System.setProperty("jna.encoding", "GBK");
            String curState = baseBiz.queryState(systemId);
            String ret = null;
            if(PREPARING.equals(curState)) {
                ret = DllInvoker.instance.dllSystemStart(systemId);
            }
            else if(PAUSING.equals(curState)) {
                ret = DllInvoker.instance.dllSystemPause(systemId);
            }
            else {
                return new ObjectRestResponse().rel(false).msg("后台状态错误，请结束仿真");
            }
            return getObjectRestResponse(systemId, ret, RUNNING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }

    /**
     * 系统暂停及继续
     */
    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemPause(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String curState = baseBiz.queryState(systemId);
            if (!RUNNING.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("后台状态错误，请结束仿真");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllSystemPause(systemId);
            return getObjectRestResponse(systemId, ret, PAUSING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }

    /**
     * 系统结束
     */
    @RequestMapping(value = "/end", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemEnd(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllSystemStop(systemId);
            baseBiz.updateState(systemId, UNREADY);
            return getObjectRestResponse(systemId, ret, UNREADY);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }

    @RequestMapping(value = "/getOnlineNodes", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse getOnlineNodes(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllGetOnlineNodes(systemId);
            Map<String, String> ret_j = (Map<String, String>) JSON.parse(ret);
            String retState = ret_j.get("success");
            String retMsg = ret_j.get("message");
            switch (retState) {
                case RT_SUCCESS:
                    return new ObjectRestResponse().rel(true).msg(retMsg);
                case RT_EXCEPTION:
                    return new ObjectRestResponse().rel(false).msg("动态库错误，请结束仿真，重新准备");
                default:
                    return new ObjectRestResponse().rel(false).msg(retMsg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }
}
