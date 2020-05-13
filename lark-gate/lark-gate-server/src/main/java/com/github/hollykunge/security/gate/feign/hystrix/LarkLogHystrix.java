package com.github.hollykunge.security.gate.feign.hystrix;

import com.github.hollykunge.security.common.feign.BaseFeignFactory;
import com.github.hollykunge.security.common.msg.FeignObjectReponse;
import com.github.hollykunge.security.gate.feign.LarkLogFeign;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description: 降级
 * @since: Create in 9:13 2020/5/11
 */
@Component
public class LarkLogHystrix extends BaseFeignFactory<LarkLogHystrix> implements LarkLogFeign {

    @Override
    public FeignObjectReponse sendKafka(TopicDto topic) {
        return getHystrixObjectReponse();
    }
}
