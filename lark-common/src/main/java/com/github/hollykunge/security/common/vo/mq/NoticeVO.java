package com.github.hollykunge.security.common.vo.mq;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息队列用公告通知实体类
 * @author zhhongyu
 * @since 2019-06-15
 */
@Data
@NoArgsConstructor
public class NoticeVO implements Serializable {
    private static final long serialVersionUID = -7128203829971899888L;

    private String id;

    private String title;

    private String type;

    private String content;

    private String top;

    private String orgCode;

    private Date sendTime;

    private String secretLevel;
}
