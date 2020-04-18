package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.FrontInputException;
import com.github.hollykunge.security.entity.Comment;
import com.github.hollykunge.security.mapper.CommentMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CommentService
 * @Description TODO
 * @Author hollykunge
 * @Date 2019/6/30 13:28
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService extends BaseBiz<CommentMapper, Comment> {
    @Override
    protected String getPageName() {
        return null;
    }

    public List<Comment> feedbackComments(String feedbackId){
        if(StringUtils.isEmpty(feedbackId)){
            throw new FrontInputException("所反馈问题的id为空。");
        }
        Comment comment = new Comment();
        comment.setFeedbackId(feedbackId);
        return mapper.select(comment);
    }
}
