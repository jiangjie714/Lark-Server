package com.github.hollykunge.security.common.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: zhhongyu
 * @description: 系统异常调用lark-log接口
 * @since: Create in 17:07 2020/5/19
 */
@FeignClient(name = "lark-log",path = "kafka")
public interface ErrorLogFeign {
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public ObjectRestResponse<Boolean> sendKafka(TopicDto topic);
}
