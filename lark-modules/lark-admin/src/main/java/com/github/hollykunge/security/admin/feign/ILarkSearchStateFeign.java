package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.admin.feign.hystrix.ILarkSearchStateHystrix;
import com.github.hollykunge.security.search.web.api.bizmd.GateLoginStatisticsApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 17:03 2020/3/19
 */
@FeignClient(value = "lark-search",fallback = ILarkSearchStateHystrix.class)
public interface ILarkSearchStateFeign extends GateLoginStatisticsApiService {
}
