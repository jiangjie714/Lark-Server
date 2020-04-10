package com.github.hollykunge.security.dto;


import lombok.Data;

import java.util.Date;

/**
 * @Author: yzq
 * @Date: 创建于 2020/4/5 16:22
 */
@Data
public class FeedBackDto {
    private String id;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private Date updTime;

    private String updUser;

    private String updName;

    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String title;

    private String content;
    /**
     * 问题状态，1-true打开，0-false关闭
     */
    private String status;
    /**
     * 问题类型
     */
    private String type;
    /**
     * 普通用户可见性(1为可见，0为不可见)
     */
    private String visible;

    /**
     * 反馈意见
     */
    private String suggestion;
}
