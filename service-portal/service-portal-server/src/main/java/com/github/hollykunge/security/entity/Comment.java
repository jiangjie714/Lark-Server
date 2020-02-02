package com.github.hollykunge.security.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName Comment
 * @Description 评论
 * @Author hollykunge
 * @Date 2019/6/29 23:26
 * @Version 1.0
 **/
@Data
@Table(name = "PORTAL_COMMENT")
public class Comment extends BaseEntity {
    /**
     * 属于哪个问题
     */
    @Column(name = "FEEDBACK_ID")
    private String feedbackId;
    @Column(name = "CONTENT")
    private String content;
}
