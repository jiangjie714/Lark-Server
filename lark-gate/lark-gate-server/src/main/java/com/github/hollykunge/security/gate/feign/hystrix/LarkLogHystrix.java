package com.github.hollykunge.security.gate.feign.hystrix;

import com.github.hollykunge.security.gate.feign.LarkLogFeign;
import com.github.hollykunge.security.log.api.dto.TopicDto;
import com.github.hollykunge.security.log.api.response.LogObjectRestResponse;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description: 降级
 * @since: Create in 9:13 2020/5/11
 */
@Component
public class LarkLogHystrix extends BaseFeignFactory<LarkLogHystrix> implements LarkLogFeign {
    @Override
    public LogObjectRestResponse sendKafka(TopicDto topic) throws Exception {
        return new LogObjectRestResponse();
    }
}
