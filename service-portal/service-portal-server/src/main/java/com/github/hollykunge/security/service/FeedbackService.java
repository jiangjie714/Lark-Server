package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.entity.Feedback;
import com.github.hollykunge.security.mapper.FeedbackMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName FeedBackService
 * @Description TODO
 * @Author hollykunge
 * @Date 2019/6/30 13:29
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class FeedbackService extends BaseBiz<FeedbackMapper, Feedback> {
    @Override
    protected String getPageName() {
        return null;
    }
}
