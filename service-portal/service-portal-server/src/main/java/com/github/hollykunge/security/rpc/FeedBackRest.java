package com.github.hollykunge.security.rpc;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.dto.FeedBackAnswerDto;
import com.github.hollykunge.security.dto.FeedBackDto;
import com.github.hollykunge.security.entity.Feedback;
import com.github.hollykunge.security.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: yzq
 * @Date: 创建于 2020/4/5 15:50
 */
@RestController
@RequestMapping("/api/feedBack")
public class FeedBackRest {

    @Autowired
    private FeedbackService feedbackService;
    /**
     * todo: 管理员答复反馈接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/answer", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<FeedBackAnswerDto> getFeedBackAnswer(@RequestParam Map<String, Object> params) {

        Query query = new Query(params);
        String crtTime = (params.get("crtTime"))==null?"":params.get("crtTime").toString();
        return feedbackService.selectByQueryToAnswer(query,crtTime);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<FeedBackDto> putFeedBackAnswer(@RequestBody FeedBackDto feedBackDto) {
        feedbackService.updateSelectiveById(feedBackDto);
        return new ObjectRestResponse<FeedBackRest>().rel(true);
    }

}
