package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.dto.FeedBackAnswerDto;
import com.github.hollykunge.security.dto.FeedBackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: yzq
 * @Date: 创建于 2020/4/5 15:55
 */
@FeignClient("service-portal")
public interface FeedBackAnswerFeign {

    @RequestMapping(value = "/api/feedBack/answer", method = RequestMethod.GET)
    TableResultResponse<FeedBackAnswerDto> getFeedBackAnswer(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "/api/feedBack", method = RequestMethod.PUT)
    ObjectRestResponse<FeedBackDto> putFeedBackAnswer(@RequestBody FeedBackDto feedback);
}
