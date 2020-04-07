package com.github.hollykunge.security.mapper;

import com.github.hollykunge.security.dto.FeedBackAnswerDto;
import com.github.hollykunge.security.entity.Feedback;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName FeedBackMapper
 * @Description TODO
 * @Author hollykunge
 * @Date 2019/6/30 13:34
 * @Version 1.0
 **/
public interface FeedbackMapper extends Mapper<Feedback> {

    /**
     * 查询全部反馈
     * @return
     */
    List<FeedBackAnswerDto> queryFeedBackAnswer(@Param("crtTime")String crtTime);
}
