package com.github.hollykunge.security.feign;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "lark-admin")
public interface IUserService {
    @RequestMapping(value = "/api/user/pid/{pid}/info", method = RequestMethod.GET)
    AdminUser getUserInfoByPid(@PathVariable("pid") String pid);
}
