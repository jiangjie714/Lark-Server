package com.github.hollykunge.security.gate.feign;

import com.github.hollykunge.security.gate.feign.hystrix.LarkLogHystrix;
import com.github.hollykunge.security.log.api.kafka.KafkaSendWebApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:12 2020/5/11
 */
@FeignClient(value = "lark-log"
        ,path = "kafka"
        ,fallbackFactory = LarkLogHystrix.class
)
public interface LarkLogFeign extends KafkaSendWebApi {
}
