package com.github.hollykunge.security.gate.feign.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.gate.feign.ILarkSearchFeign;
import com.github.hollykunge.security.search.dto.TopicDto;
import com.github.hollykunge.security.search.web.api.response.SearchObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:54 2020/3/16
 */
@Slf4j
@Component
public class LarkSearchHystrix implements ILarkSearchFeign {
    @Override
    public SearchObjectRestResponse sendKafka(TopicDto topic) throws Exception {
        log.error("发送搜索服务日志信息失败!!!!{}", JSONObject.toJSONString(topic));
        return null;
    }
}
