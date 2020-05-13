package com.github.hollykunge.security.gate.feign;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.common.msg.FeignListResponse;
import com.github.hollykunge.security.gate.feign.hystrix.AdminUserHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: zhhongyu
 * @description: 调用admin服务的接口
 * @since: Create in 13:11 2020/4/28
 */
@FeignClient(value = "lark-admin",path = "api",fallbackFactory = AdminUserHystrix.class)
public interface AdminUserFeign {
    /**
     * 根据userId获取用户权限列表
     *
     * @param userId 用户id
     * @return List<FrontPermission>
     */
    @RequestMapping(value = "/user/un/{userId}/permissions", method = RequestMethod.GET)
    @ResponseBody
    FeignListResponse<List<FrontPermission>> getPermissionByUserId(@PathVariable("userId") String userId);
}
