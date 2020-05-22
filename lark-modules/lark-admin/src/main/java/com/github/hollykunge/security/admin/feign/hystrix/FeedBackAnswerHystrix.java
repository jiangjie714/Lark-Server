package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.FeedBackAnswerFeign;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.portal.dto.FeedBackAnswerDto;
import com.github.hollykunge.security.portal.dto.FeedBackDto;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 服务降级
 * @since: Create in 14:44 2020/5/18
 */
@Component
public class FeedBackAnswerHystrix extends BaseHystrixFactory<FeedBackAnswerHystrix> implements FeedBackAnswerFeign {
    @Override
    public TableResultResponse<FeedBackAnswerDto> getFeedBackAnswer(Map<String, Object> params) {
        return getHystrixTableReponse();
    }

    @Override
    public ObjectRestResponse<FeedBackDto> putFeedBackAnswer(FeedBackDto feedback) {
        return getHystrixObjectReponse();
    }
}
