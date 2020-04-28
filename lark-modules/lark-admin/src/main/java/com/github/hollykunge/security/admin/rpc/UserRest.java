package com.github.hollykunge.security.admin.rpc;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.dto.ChangeUserPwdDto;
import com.github.hollykunge.security.admin.api.dto.OrgUser;
import com.github.hollykunge.security.admin.api.rest.AdminUserRpcRest;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.biz.PositionBiz;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.rpc.service.PermissionService;
import com.github.hollykunge.security.admin.rpc.service.UserRestService;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统管理对内部服务接口和需要被网关忽略权限校验的接口
 *
 * <p>该类创建接口约束行为如下：<p/>
 * <p>1、对外提供的接口中的@RequestMapping要加在父类中的方法上。</p>
 * <p>2、需要提供给前端，并对网关忽略权限校验的接口，正常定义在该类中</p>
 *
 * @author 协同设计小组
 * @date 2017-06-21 8:15
 */
@RestController
@RequestMapping("api")
public class UserRest implements AdminUserRpcRest {

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

    public List<FrontPermission> getAllPermission() {
        return permissionService.getAllPermission();
    }

    @Override
    public List<FrontPermission> getPermissionByUserId(@PathVariable("userId") String userId) {
        return permissionService.getPermissionByUserId(userId);
    }

    @Override
    public AdminUser validate(String pid, String password) {
        return permissionService.validate(pid, password);
    }

    @Override
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
    public List<AdminUser> getUserListByPosAndSec(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel) {
        return positionBiz.getPositionUsersBySecret(positionId, secretLevel);
    }

    /**
     * 前端接口
     * @param positionId
     * @param secretLevel
     * @param orgCode
     * @return
     */
    @RequestMapping(value = "/user/{positionId}/{secretLevel}/{orgCode}/list", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> getUserListByPosAndSecAndOrg(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel, @PathVariable("orgCode") String orgCode) {
        List<AdminUser> users = positionBiz.getPositionUsers(positionId, secretLevel, orgCode);
        return new ListRestResponse("", users.size(), users);
    }

    /**
     * 前端接口
     * @param orgCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/contacts/{orgCode}", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<OrgUser>> getContactsByOrg(@PathVariable("orgCode") String orgCode) throws Exception {
        if (StringUtils.isEmpty(orgCode)) {
            orgCode = AdminCommonConstant.ROOT;
        }
        List<OrgUser> orgUsers = orgBiz.getChildOrgUser(orgCode);
        return new ListRestResponse("", orgUsers.size(), orgUsers);
    }

    /**
     * 用户修改密码接口
     * @param changeUserPwdDto
     * @return
     */
    @RequestMapping(value = "/user/pwd",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Boolean> changeUserPwd(@RequestBody @Valid ChangeUserPwdDto changeUserPwdDto, HttpServletRequest request) throws Exception {
        userBiz.changeUserPwd(changeUserPwdDto,request);
        return new ObjectRestResponse<>().data(true).rel(true);
    }
}
