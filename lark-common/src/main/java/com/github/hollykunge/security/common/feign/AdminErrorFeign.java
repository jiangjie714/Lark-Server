package com.github.hollykunge.security.common.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: zhhongyu
 * @description: 数据落地到admin服务表
 * @since: Create in 14:08 2020/5/20
 */
@FeignClient(name = "lark-admin")
public interface AdminErrorFeign {
    @RequestMapping(value = "sysError",method = RequestMethod.POST)
    ObjectRestResponse<Boolean> saveLog(@RequestBody ErrorMessageEntity errorMessageEntity);
}
