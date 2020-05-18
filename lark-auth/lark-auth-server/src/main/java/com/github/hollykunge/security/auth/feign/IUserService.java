package com.github.hollykunge.security.auth.feign;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.auth.configuration.FeignConfiguration;
import com.github.hollykunge.security.auth.feign.hystrix.IUserHystrix;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-21 8:11
 */
@FeignClient(value = "lark-admin",configuration = FeignConfiguration.class,fallbackFactory = IUserHystrix.class)
public interface IUserService {
  /**
   * 验证用户
   * @param pid
   * @param password
   * @return
   */
  @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
  ObjectRestResponse<AdminUser> validate(@RequestParam("pid") String pid, @RequestParam("password") String password);
}
