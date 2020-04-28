package com.github.hollykunge.security.gate.feign.hystrix;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.gate.feign.AdminUserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhhongyu
 * @description: 调用admin服务降级
 * @since: Create in 13:52 2020/4/28
 */
@Component
@Slf4j
public class AdminUserHystrix extends BaseFeignFactory<AdminUserHystrix> implements AdminUserFeign {
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
}
