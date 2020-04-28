package com.github.hollykunge.security.gate.feign.hystrix;

import com.github.hollykunge.security.gate.feign.ILarkSearchFeign;
import com.github.hollykunge.security.search.dto.TopicDto;
import com.github.hollykunge.security.search.web.api.response.SearchObjectRestResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description: 网关服务降级类
 * @since: Create in 15:30 2020/4/27
 */
@Component
public class LarkSearchHytix extends BaseFeignFactory<LarkSearchHytix> implements ILarkSearchFeign {

    @Override
    public SearchObjectRestResponse sendKafka(TopicDto topic) throws Exception {
        String message = ExceptionUtils.getMessage(throwable);
        SearchObjectRestResponse searchObjectRestResponse = new SearchObjectRestResponse();
        searchObjectRestResponse.setMessage(message);
        searchObjectRestResponse.setStatus(500);
        return searchObjectRestResponse;
    }
}
