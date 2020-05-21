package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_NOTIFY")
public class LarkNotify extends BaseEntity {

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NOTIFY_FROM")
    private String notifyFrom;

    @Column(name = "NOTIFY_TO")
    private String notifyTo;

    @Column(name = "IS_READ")
    private String isRead;

    @Column(name = "READ_TIME")
    private String readTime;

    @Column(name = "FINALLY_SEND_TIME")
    private Date finallySendTime;

    @Column(name = "SEND_TIME")
    private Date sendTime;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "TERMINAL")
    private String terminal;

    @Column(name = "FROM_TYPE")
    private String fromType;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "SOURCE_CODE")
    private String sourceCode;

    @Column(name = "SEND_DATA")
    private String sendData;

}