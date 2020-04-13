package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.feign.FeedBackAnswerFeign;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.dto.FeedBackAnswerDto;
import com.github.hollykunge.security.dto.FeedBackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: yzq
 * @Date: 创建于 2020/4/5 15:59
 */
@Controller
@RequestMapping("/feedBack")
public class FeedBackAnswerController {

    @Autowired
    private FeedBackAnswerFeign feedBackAnswerFeign;

    @RequestMapping(value = "/answer", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<FeedBackAnswerDto> getFeedBackAnswer(@RequestParam Map<String, Object> params) {

        return feedBackAnswerFeign.getFeedBackAnswer(params);
    }


    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<FeedBackDto> getFeedBackAnswer(@RequestBody FeedBackDto feedBackDto) {

        return feedBackAnswerFeign.putFeedBackAnswer(feedBackDto);
    }

}
