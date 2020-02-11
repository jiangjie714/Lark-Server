package com.workhub.z.servicechat.feign;

import com.workhub.z.servicechat.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "lark-admin")
@Repository
public interface IUserService {
    @RequestMapping(value = "/api/user/list", method = RequestMethod.GET)
    public List<UserInfo> userList(@RequestParam("userIds") String userIdSet);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public UserInfo getUserInfo(@RequestParam Map<String,String> map);
}
