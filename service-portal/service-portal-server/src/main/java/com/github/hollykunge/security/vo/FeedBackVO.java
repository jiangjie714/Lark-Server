package com.github.hollykunge.security.vo;

import com.github.hollykunge.security.entity.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: 问题反馈前端实体
 * @author: dd
 * @since: 2019-06-30
 */
@Data
public class FeedBackVO {
    private String feedBackId;
    private String title;
    private String content;
    private Boolean status;
    private String type;
    private Date crtTime;
    private Date updTime;
    private String crtUser;
    private String updUser;
    private Integer likeNum;
    private List<Comment> commentList;
}
