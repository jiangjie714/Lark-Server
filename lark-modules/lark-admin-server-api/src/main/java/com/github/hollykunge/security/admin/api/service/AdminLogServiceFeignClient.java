package com.github.hollykunge.security.admin.api.service;

import com.github.hollykunge.security.admin.api.hystrix.AdminLogServiceFallback;
import com.github.hollykunge.security.admin.api.log.LogInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "lark-admin",fallback = AdminLogServiceFallback.class)
public interface AdminLogServiceFeignClient {
  @RequestMapping(value="/api/log/save",method = RequestMethod.POST)
  public void saveLog(LogInfo info);
}