package com.github.hollykunge.security.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 *@ClassName FeedBack
 *@Description 问题反馈
 *@Author hollykunge
 *@Date
 *@Version 1.0
 **/
@Data
@Table(name = "PORTAL_FEEDBACK")
public class Feedback extends BaseEntity {
    @Column(name = "TITLE")
    private String title;
    @Column(name = "CONTENT")
    private String content;
    /**
     * 问题状态，1-true打开，0-false关闭
     */
    @Column(name = "STATUS")
    private String status;
    /**
     * 问题类型
     */
    @Column(name = "TYPE")
    private String type;
    /**
     * 普通用户可见性(1为可见，0为不可见)
     */
    @Column(name = "VISIBLE")
    private String visible;

    /**
     * 反馈意见
     */
    @Column(name = "SUGGESTION")
    private String suggestion;
}
