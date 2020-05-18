package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.admin.feign.hystrix.PortalServerHystrix;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.portal.dto.CommonToolsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 调用门户服务的feign客户端
 * @since: Create in 14:40 2019/11/14
 */
@FeignClient(value = "service-portal",path = "api",fallbackFactory = PortalServerHystrix.class)
public interface PortalServerFeign {
    /**
     * 用户常用工具接口
     * fansq 19-12-11修改
     * 修改参数类型为map
     */
    @RequestMapping(value = "/commonTools/page", method = RequestMethod.POST)
    TableResultResponse<CommonToolsDto> page(@RequestBody Map<String, Object> params);

    /**
     * fansq 19-12-12
     * 修改参数类型为map
     */
    @RequestMapping(value = "/commonTools", method = RequestMethod.POST)
    ObjectRestResponse<CommonToolsDto> update(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "/commonTools/delete", method = RequestMethod.GET)
    ObjectRestResponse<CommonToolsDto> remove(@RequestParam("id") String id);

    @RequestMapping(value = "/commonTools/add", method = RequestMethod.POST)
    ObjectRestResponse<CommonToolsDto> add(@RequestBody HashMap<String,Object> hashMap);
    /**
     * 用户常用工具接口结束
     */
}
