package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.dto.FeedBackAnswerDto;
import com.github.hollykunge.security.dto.FeedBackDto;
import com.github.hollykunge.security.entity.Feedback;
import com.github.hollykunge.security.mapper.FeedbackMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public TableResultResponse<FeedBackAnswerDto> selectByQueryToAnswer(Query query,String crtTime) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String time = crtTime.replaceAll("\"","");
        Date date;
        String sDate = "";
        try {
            if (!crtTime.isEmpty()) {
                date = formatter.parse(crtTime.replaceAll("\"",""));
                 sDate = formatter.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<FeedBackAnswerDto> list = mapper.queryFeedBackAnswer(sDate);
        return new TableResultResponse<FeedBackAnswerDto>(result.getPageSize(), result.getPageNum() ,result.getPages(), result.getTotal(), list);
    }

    public void updateSelectiveById(FeedBackDto feedBackDto) {
        Feedback feedback =new Feedback();
        BeanUtils.copyProperties(feedBackDto,feedback);
        super.updateSelectiveById(feedback);
    }
}
