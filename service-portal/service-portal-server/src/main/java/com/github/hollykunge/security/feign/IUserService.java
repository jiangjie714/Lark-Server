package com.github.hollykunge.security.feign;

import com.rabbitmq.http.client.domain.UserInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ace-admin")
public interface IUserService {
    @RequestMapping(value = "/api/user/info", method = RequestMethod.POST)
    public UserInfo userInfo(@RequestParam("userPId") String userPId);
}
