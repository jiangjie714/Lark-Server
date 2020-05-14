package com.github.hollykunge.security.gate.feign.hystrix;

import com.github.hollykunge.security.admin.dto.authority.FrontPermission;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.FeignListResponse;
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
public class AdminUserHystrix extends BaseHystrixFactory<AdminUserHystrix> implements AdminUserFeign {

    @Override
    public FeignListResponse<List<FrontPermission>> getPermissionByUserId(String userId) {
        return getHystrixListResponse();
    }
}
