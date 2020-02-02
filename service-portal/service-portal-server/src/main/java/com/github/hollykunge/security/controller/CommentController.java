package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.Comment;
import com.github.hollykunge.security.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CommentController
 * @Description 评论
 * @Author hollykunge
 * @Date 2019/6/30 13:27
 * @Version 1.0
 **/
@RestController
@RequestMapping("comment")
public class CommentController extends BaseController<CommentService, Comment> {
    /**
     * todo:使用
     * 根据问题反馈id获取对应的评论列表
     * @param feedbackId 问题反馈id
     * @return
     */
    @RequestMapping(value = "feedback", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Comment>> feedbackComments(@RequestParam String feedbackId){
        List<Comment> comments = baseBiz.feedbackComments(feedbackId);
        return new ListRestResponse<List<Comment>>("",comments.size(),comments);
    }
}
