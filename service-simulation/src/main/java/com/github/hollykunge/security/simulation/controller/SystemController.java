package com.github.hollykunge.security.simulation.controller;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.simulation.biz.DllInvoker;
import com.github.hollykunge.security.simulation.biz.SystemBiz;
import com.github.hollykunge.security.simulation.entity.SimuSystem;
import com.github.hollykunge.security.simulation.vo.ModelConfigVo;
import com.github.hollykunge.security.simulation.vo.SimuUser;
import com.github.hollykunge.security.simulation.vo.UserSystemsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.hollykunge.security.simulation.config.Constant.*;

/**
 * @author jihang
 */

@RestController
@RequestMapping("/simuSystem")
public class SystemController extends BaseController<SystemBiz, SimuSystem> {

    @Autowired
    @Lazy
    private AdminUserServiceFeignClient userService;

    @RequestMapping(value = "/userSimuList", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<SimuUser>> userListSimu(@RequestParam("id") String userIds) {
        List<SimuUser> simuUsers = new ArrayList<>();
        if (!StringUtils.isEmpty(userIds)) {
            String[] ids = userIds.split(",");
            for (String m : ids) {
                AdminUser user = userService.getUserInfoByPid(m);
                SimuUser info = new SimuUser();
                if (user == null) {
                    continue;
                }
                BeanUtils.copyProperties(user, info);
                simuUsers.add(info);
            }
        }
        return new ListRestResponse("", simuUsers.size(), simuUsers);
    }

    /**
     * 增加
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse create(@RequestBody SimuSystem entity) throws Exception {
        int ret = baseBiz.createSystem(entity);
        switch (ret) {
            case NAME_REGULATION_WRONG:
                return new ObjectRestResponse().rel(false).msg("名称不符合规范");
            default:
                return new ObjectRestResponse().rel(true).msg("");
        }
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse update(@RequestBody SimuSystem entity) {
        int ret = baseBiz.updateSystem(entity);
        switch (ret) {
            case NAME_REGULATION_WRONG:
                return new ObjectRestResponse().rel(false).msg("名称不符合规范");
            default:
                return new ObjectRestResponse().rel(true).msg("");
        }
    }

    /**
     * 查找用户
     */
    @RequestMapping(value = "/queryByUser", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<UserSystemsVo>> queryByUser(@RequestParam("id") String userId) {
        List<UserSystemsVo> userSystemsVos = baseBiz.queryByUser(userId);
        return new ListRestResponse("", userSystemsVos.size(), userSystemsVos);
    }

    /**
     * 系统准备
     */
    @RequestMapping(value = "/systemReady", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemReady(
            @RequestParam("systemId") String systemId, @RequestParam("users") String users) {
        try {
            if (systemId == null || systemId.equals("") || users == null || users.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真，重新准备");
            }
            String curState = baseBiz.queryState(systemId);
            if (!UNREADY.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("后台状态错误，请结束仿真，重新准备");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllSystemPrepare(systemId, 0.0, 0.1, users);
            return getObjectRestResponse(systemId, ret, PREPARING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真，重新准备");
        }
    }

    /**
     * 系统开始
     */
    @RequestMapping(value = "/systemStart", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemStart(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String curState = baseBiz.queryState(systemId);
            if (!PREPARING.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("后台状态错误，请结束仿真");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllSystemStart(systemId);
            return getObjectRestResponse(systemId, ret, RUNNING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }

    /**
     * 系统暂停及继续
     */
    @RequestMapping(value = "/systemPause", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse systemPause(@RequestParam("systemId") String systemId) {
        try {
            if (systemId == null || systemId.equals("")) {
                return new ObjectRestResponse().rel(false).msg("传入后台参数错误，请结束仿真");
            }
            String curState = baseBiz.queryState(systemId);
            if (UNREADY.equals(curState) || PREPARING.equals(curState)) {
                return new ObjectRestResponse().rel(false).msg("后台状态错误，请结束仿真");
            }
            System.setProperty("jna.encoding", "GBK");
            String ret = DllInvoker.instance.dllSystemPause(systemId);
            if (RUNNING.equals(curState)) {
                return getObjectRestResponse(systemId, ret, PAUSING);
            } else {
                return getObjectRestResponse(systemId, ret, RUNNING);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ObjectRestResponse().rel(false).msg("系统后台错误，请结束仿真");
        }
    }

    /**
     * 系统结束
     */
    @RequestMapping(value = "/systemEnd", method = RequestMethod.POST)
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

    /**
     * 配置文件生成及下载
     */
    @RequestMapping(value = "/downloadModelConfig", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse downloadModelConfig(@RequestBody ModelConfigVo entity) {
        return new ObjectRestResponse().rel(true).msg("");
    }
}
