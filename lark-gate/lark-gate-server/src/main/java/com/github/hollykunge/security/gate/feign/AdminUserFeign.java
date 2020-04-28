package com.github.hollykunge.security.gate.feign;

import com.github.hollykunge.security.admin.api.rest.AdminUserRpcRest;
import com.github.hollykunge.security.gate.feign.hystrix.AdminUserHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: zhhongyu
 * @description: 调用admin服务的接口
 * @since: Create in 13:11 2020/4/28
 */
@FeignClient(value = "lark-admin",path = "api",fallback = AdminUserHystrix.class)
public interface AdminUserFeign extends AdminUserRpcRest {
}
