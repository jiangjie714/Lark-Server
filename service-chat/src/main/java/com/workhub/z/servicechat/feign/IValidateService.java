package com.workhub.z.servicechat.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
*@Description: token认证类
*@Author: 忠
*@date: 2019/4/11
*/
@FeignClient(value = "lark-auth")
@Repository
public interface IValidateService {
    void validate(String token) throws Exception;

    @RequestMapping(value = "/api/token/generate", method = RequestMethod.GET)
    public ObjectRestResponse generate(@RequestParam("username") String username,@RequestParam("password") String password) throws Exception;
}
