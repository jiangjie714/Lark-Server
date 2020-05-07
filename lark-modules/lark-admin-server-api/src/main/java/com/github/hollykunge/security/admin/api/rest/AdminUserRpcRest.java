package com.github.hollykunge.security.admin.api.rest;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AdminUserRpcRest {
    /**
     * 根据userId获取用户权限列表
     *
     * @param userId 用户id
     * @return List<FrontPermission>
     */
    @RequestMapping(value = "/user/un/{userId}/permissions", method = RequestMethod.GET)
    @ResponseBody
    List<FrontPermission> getPermissionByUserId(@PathVariable("userId") String userId);

    /**
     * 获取所有的权限列表
     *
     * @return List<FrontPermission>
     */
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    @ResponseBody
    List<FrontPermission> getAllPermission();

    /**
     * 根据userId获取用户信息
     *
     * @param userId 用户id
     * @return AdminUser
     */
    @RequestMapping(value = "/user/userId/{id}/info", method = RequestMethod.GET)
    @ResponseBody
    AdminUser getUserInfoByUserId(@PathVariable("id") String userId);

    /**
     * 根据身份证号获取用户信息
     *
     * @param pid 身份证号
     * @return AdminUser
     */
    @RequestMapping(value = "/user/pid/{id}/info", method = RequestMethod.GET)
    @ResponseBody
    AdminUser getUserInfoByPid(@PathVariable("id") String pid);


    /**
     * 验证用户名密码
     *
     * @param pid      身份证号
     * @param password 密码
     * @return AdminUser
     */
    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    @ResponseBody
    AdminUser validate(@RequestParam("pid") String pid, @RequestParam("password") String password);


    /**
     * 根据ids获取用户信息，ids可为pid或userId
     *
     * @param ids 根据人员userIds（由逗号拼接）获取人员集（带缓存）
     * @return List<AdminUser>
     */
    @RequestMapping(value = "/user/{ids}/list", method = RequestMethod.GET)
    @ResponseBody
    List<AdminUser> getUserListByIds(@PathVariable("ids") String ids);


    /**
     * 根据岗位id和密级获取人员列表
     *
     * @param positionId  岗位id
     * @param secretLevel 密级
     * @return List<AdminUser>
     */
    @RequestMapping(value = "/user/{positionId}/{secretLevel}/list", method = RequestMethod.GET)
    @ResponseBody
    List<AdminUser> getUserListByPosAndSec(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel);
}
