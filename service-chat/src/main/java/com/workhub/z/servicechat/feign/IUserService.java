package com.workhub.z.servicechat.feign;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "lark-admin")
@Repository
public interface IUserService {
    @RequestMapping(value="/api/user/{ids}/list", method = RequestMethod.GET)
    List<AdminUser> userList(@PathVariable("ids") String userIds);

    @RequestMapping(value = "/api/user/pid/{pid}/info", method = RequestMethod.GET)
    AdminUser getUserInfoByPid(@PathVariable("pid") String pid);

    @RequestMapping(value = "/api/user/userId/{id}/info", method = RequestMethod.GET)
    AdminUser getUserInfo(@PathVariable("id") String userId);
}
