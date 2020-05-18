package com.github.hollykunge.security.gate.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.gate.feign.hystrix.LarkLogHystrix;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:12 2020/5/11
 */
@FeignClient(value = "lark-log"
        ,path = "kafka"
        ,fallbackFactory = LarkLogHystrix.class
)
public interface LarkLogFeign {
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    ObjectRestResponse sendKafka(TopicDto topic);
}
