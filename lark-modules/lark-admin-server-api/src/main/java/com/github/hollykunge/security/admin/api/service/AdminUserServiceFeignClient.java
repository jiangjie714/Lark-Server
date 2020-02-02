package com.github.hollykunge.security.admin.api.service;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.dto.OrgUser;
import com.github.hollykunge.security.admin.api.hystrix.AdminUserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LARK
 */
@FeignClient(value = "lark-admin",fallback = AdminUserServiceFallback.class)
public interface AdminUserServiceFeignClient {
  /**
   * 根据userId获取用户权限列表
   * @param userId 用户id
   * @return List<FrontPermission>
   */
  @RequestMapping(value="/api/user/un/{userId}/permissions",method = RequestMethod.GET)
  List<FrontPermission> getPermissionByUserId(@PathVariable("userId") String userId);

  /**
   * 获取所有的权限列表
   * @return List<FrontPermission>
   */
  @RequestMapping(value="/api/permissions",method = RequestMethod.GET)
  List<FrontPermission> getAllPermission();

  /**
   * 根据userId获取用户信息
   * @param userId 用户id
   * @return AdminUser
   */
  @RequestMapping(value="/api/user/userId/{id}/info",method = RequestMethod.GET)
  AdminUser getUserInfoByUserId(@PathVariable("id") String userId);

  /**
   * 根据身份证号获取用户信息
   * @param pid 身份证号
   * @return AdminUser
   */
  @RequestMapping(value="/api/user/pid/{id}/info", method = RequestMethod.GET)
  AdminUser getUserInfoByPid(@PathVariable("id") String pid);


  /**
   * 验证用户名密码
   * @param pid 身份证号
   * @param password 密码
   * @return AdminUser
   */
  @RequestMapping(value = "api/user/validate", method = RequestMethod.POST)
  AdminUser validate(String pid, String password);


  /**
   * 根据ids获取用户信息，ids可为pid或userId
   * @param ids 根据人员userIds（由逗号拼接）获取人员集（带缓存）
   * @return List<AdminUser>
   */
  @RequestMapping(value="/api/user/{ids}/list", method = RequestMethod.GET)
  List<AdminUser> getUserListByIds(@PathVariable("ids") String ids);


  /**
   * 根据岗位id和密级获取人员列表
   * @param positionId 岗位id
   * @param secretLevel 密级
   * @return List<AdminUser>
   */
  @RequestMapping(value="/api/user/{positionId}/{secretLevel}/list", method = RequestMethod.GET)
  List<AdminUser> getUserListByPosAndSec(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel);

  /**
   * 根据岗位id、密级、组织编码获取人员列表
   * @param positionId 岗位id
   * @param secretLevel 密级
   * @param orgCode 组织编码
   * @return List<AdminUser>
   */
  @RequestMapping(value="/api/user/{positionId}/{secretLevel}/{orgCode}/list", method = RequestMethod.GET)
  List<AdminUser> getUserListByPosAndSecAndOrg(@PathVariable("positionId") String positionId, @PathVariable("secretLevel") String secretLevel, @PathVariable("orgCode") String orgCode);

  /**
   * 根据组织编码获取通讯录
   * @param orgCode 组织编码
   * @return List<OrgUser>
   * @throws Exception 查询异常
   */
  @RequestMapping(value = "/{orgCode}/contacts", method = RequestMethod.GET)
  List<OrgUser> getContactsByOrg(@PathVariable("orgCode") String orgCode) throws Exception;
}