package com.github.hollykunge.security.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author: yzq
 * @Date: 创建于 2020/4/5 11:50
 */
@Data
public class FeedBackAnswerDto {

    private String id;
    private String pathName;
    private String crtName;
    private String title;
    private String content;
    private Date crtTime;
    private String suggestion;

}
