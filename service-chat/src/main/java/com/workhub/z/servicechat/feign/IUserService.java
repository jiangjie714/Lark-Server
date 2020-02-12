package com.workhub.z.servicechat.feign;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.workhub.z.servicechat.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "lark-admin")
@Repository
public interface IUserService {
    @RequestMapping(value="/api/user/{ids}/list", method = RequestMethod.GET)
    List<AdminUser> userList(@PathVariable("ids") String ids);

    @RequestMapping(value="/api/user/pid/{id}/info", method = RequestMethod.GET)
    AdminUser getUserInfo(@PathVariable("id") String pid);
}
