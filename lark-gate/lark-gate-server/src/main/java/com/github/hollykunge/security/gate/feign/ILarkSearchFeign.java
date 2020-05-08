package com.github.hollykunge.security.gate.feign;

import com.github.hollykunge.security.gate.feign.hystrix.LarkSearchHytix;
import com.github.hollykunge.security.search.web.api.kafkamd.KafkaSendWebApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:53 2020/3/16
 */
@FeignClient(value = "lark-search"
        ,fallbackFactory = LarkSearchHytix.class
)
public interface ILarkSearchFeign extends KafkaSendWebApi {
}