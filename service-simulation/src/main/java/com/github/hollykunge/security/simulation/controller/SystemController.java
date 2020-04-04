package com.github.hollykunge.security.simulation.controller;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.simulation.biz.SystemBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.vo.SimuUser;
import com.github.hollykunge.security.simulation.vo.UserSystemsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jihang
 */

@RestController
@RequestMapping("/system")
public class SystemController extends BaseController<SystemBiz, SystemInfo> {

    @Autowired
    @Lazy
    private AdminUserServiceFeignClient userService;

    /**
     * 查找系统的用户们
     */
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<SimuUser>> userList(@RequestParam("id") String userIds) {
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
     * 查找用户的系统们
     */
    @RequestMapping(value = "/queryByUser", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<UserSystemsVo>> queryByUser(@RequestParam("id") String userId) {
        List<UserSystemsVo> userSystemsVos = baseBiz.queryByUser(userId);
        return new ListRestResponse("", userSystemsVos.size(), userSystemsVos);
    }

}
