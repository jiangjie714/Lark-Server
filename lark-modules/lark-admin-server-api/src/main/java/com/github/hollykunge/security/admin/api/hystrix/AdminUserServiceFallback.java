package com.github.hollykunge.security.admin.api.hystrix;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.dto.OrgUser;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 服务降级
 * @author LARK
 */
@Component
@Slf4j
public class AdminUserServiceFallback implements AdminUserServiceFeignClient {
    @Override
    public List<FrontPermission> getPermissionByUserId(String userId) {
        log.error("ERROR LARK INVOKE: {}, {}", "getPermissionByUserId", userId);
        return null;
    }

    @Override
    public List<FrontPermission> getAllPermission() {
        log.error("ERROR LARK INVOKE: {}", "getPermissionByUsername");
        return null;
    }

    @Override
    public AdminUser getUserInfoByUserId(String userId) {
        log.error("ERROR LARK INVOKE: {}, {}", "getUserInfoByUserId", userId);
        return null;
    }

    @Override
    public AdminUser getUserInfoByPid(String pid) {
        log.error("ERROR LARK INVOKE: {}, {}", "getUserInfoByPid", pid);
        return null;
    }

    @Override
    public AdminUser validate(String pid, String password) {
        log.error("ERROR LARK INVOKE: {}, {}, {}", "validate", pid, password);
        return null;
    }

    @Override
    public List<AdminUser> getUserListByIds(String ids) {
        log.error("ERROR LARK INVOKE: {}, {}", "getPermissionByUsername", ids);
        return null;
    }

    @Override
    public List<AdminUser> getUserListByPosAndSec(String positionId, String secretLevel) {
        log.error("ERROR LARK INVOKE: {}, {}, {}", "getPermissionByUsername", positionId, secretLevel);
        return null;
    }

    @Override
    public List<AdminUser> getUserListByPosAndSecAndOrg(String positionId, String secretLevel, String orgCode) {
        log.error("ERROR LARK INVOKE: {}, {}, {}", "getPermissionByUsername", secretLevel, orgCode);
        return null;
    }

    @Override
    public List<OrgUser> getContactsByOrg(String orgCode) throws Exception {
        log.error("ERROR LARK INVOKE: {}, {}", "getPermissionByUsername", orgCode);
        return null;
    }
}