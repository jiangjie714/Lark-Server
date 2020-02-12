package com.github.hollykunge.security.admin.rpc;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.dto.OrgUser;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.biz.PositionBiz;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.rpc.service.PermissionService;
import com.github.hollykunge.security.admin.rpc.service.UserRestService;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统管理对内部服务接口（包括权限接口、用户接口）
 *
 * @author 协同设计小组
 * @date 2017-06-21 8:15
 */
@RestController
@RequestMapping("api")
public class UserRest implements AdminUserServiceFeignClient {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserBiz userBiz;

    @Autowired
    private OrgBiz orgBiz;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private PositionBiz positionBiz;

    @Override
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    @ResponseBody
    public List<FrontPermission> getAllPermission() {
        return permissionService.getAllPermission();
    }

    @Override
    @RequestMapping(value = "/user/un/{userId}/permissions", method = RequestMethod.GET)
    @ResponseBody
    public List<FrontPermission> getPermissionByUserId(@PathVariable("userId") String userId) {
        return permissionService.getPermissionByUserId(userId);
    }

    @Override
    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    @ResponseBody
    public AdminUser validate(String pid, String password) {
        return permissionService.validate(pid, password);
    }

    @Override
    @RequestMapping(value = "/user/pid/{id}/info", method = RequestMethod.GET)
    @ResponseBody
    public AdminUser getUserInfoByPid(@PathVariable("id") String pid) {
        User user = userBiz.getUserByUserPid(pid);
        AdminUser info = new AdminUser();
        if (user == null) {
            return info;
        }
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId());
        return info;
    }

    @Override
    @RequestMapping(value = "/user/userId/{id}/info", method = RequestMethod.GET)
    @ResponseBody
    public AdminUser getUserInfoByUserId(@PathVariable("id") String userId) {
        User user = userBiz.getUserByUserId(userId);
        AdminUser info = new AdminUser();
        if (user == null) {
            return info;
        }
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId());
        return info;
    }

    @Override
    @FilterByDeletedAndOrderHandler
    @RequestMapping(value = "/user/{ids}/list", method = RequestMethod.GET)
    @ResponseBody
    public List<AdminUser> getUserListByIds(@PathVariable("ids") String userIds) {
        List<AdminUser> userInfos = new ArrayList<AdminUser>();
        if (!StringUtils.isEmpty(userIds)) {
            String[] ids = userIds.split(",");
            for (String m : ids) {
                String userInfo = userRestService.getUserInfo(null, m);
                List<AdminUser> adminUsers = JSONArray.parseArray(userInfo, AdminUser.class);
                userInfos.addAll(adminUsers);
            }
        }
        return userInfos;
    }

    @Override
    @RequestMapping(value = "/user/{positionId}/{secretLevel}/list", method = RequestMethod.GET)
    @ResponseBody
    public List<AdminUser> getUserListByPosAndSec(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel) {
        return positionBiz.getPositionUsersBySecret(positionId, secretLevel);
    }

    @Override
    @RequestMapping(value = "/user/{positionId}/{secretLevel}/{orgCode}/list", method = RequestMethod.GET)
    @ResponseBody
    public List<AdminUser> getUserListByPosAndSecAndOrg(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel, @PathVariable("orgCode") String orgCode) {
        return positionBiz.getPositionUsers(positionId, secretLevel, orgCode);
    }

    @RequestMapping(value = "/contacts/{orgCode}", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<OrgUser>> getContactsByOrg(@PathVariable("orgCode") String orgCode) throws Exception {
        if (StringUtils.isEmpty(orgCode)) {
            orgCode = AdminCommonConstant.ROOT;
        }
        List<OrgUser> orgUsers = orgBiz.getChildOrgUser(orgCode);
        return new ListRestResponse("", orgUsers.size(), orgUsers);
    }
}
