package com.github.hollykunge.security.admin.api.hystrix;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@Slf4j
public class AdminUserServiceFallback implements AdminUserServiceFeignClient {
    @Override
    public List<FrontPermission> getPermissionByUserId(@PathVariable("userId") String userId) {
        log.error("调用{}异常{}","getPermissionByUserId",userId);
        return null;
    }

    @Override
    public List<FrontPermission> getAllPermission() {
        log.error("调用{}异常","getPermissionByUsername");
        return null;
    }
}