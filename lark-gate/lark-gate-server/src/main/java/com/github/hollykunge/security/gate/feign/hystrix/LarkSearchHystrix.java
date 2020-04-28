package com.github.hollykunge.security.gate.feign.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.gate.feign.ILarkSearchFeign;
import com.github.hollykunge.security.search.dto.TopicDto;
import com.github.hollykunge.security.search.web.api.response.SearchObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:54 2020/3/16
 */
@Slf4j
@Component
public class LarkSearchHystrix implements ILarkSearchFeign {
    private Throwable throwable;

    public void setThrowable(Throwable throwable){
        this.throwable = throwable;
    }

    @Override
    public SearchObjectRestResponse sendKafka(TopicDto topic) throws Exception {
        String message = ExceptionUtils.getMessage(throwable);
        SearchObjectRestResponse searchObjectRestResponse = new SearchObjectRestResponse();
        searchObjectRestResponse.setMessage(message);
        searchObjectRestResponse.setStatus(500);
        return searchObjectRestResponse;
    }
}
