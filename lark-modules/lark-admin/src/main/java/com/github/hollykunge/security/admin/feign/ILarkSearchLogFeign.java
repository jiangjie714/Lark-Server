package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.admin.feign.hystrix.ILarkSearchLogHystrix;
import com.github.hollykunge.security.search.web.api.bizmd.GateLogAPIService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: zhhongyu
 * @description: 搜索服务接口调用
 * @since: Create in 15:09 2020/3/17
 */
@FeignClient(value = "lark-search",fallback = ILarkSearchLogHystrix.class)
public interface ILarkSearchLogFeign extends GateLogAPIService {
}
