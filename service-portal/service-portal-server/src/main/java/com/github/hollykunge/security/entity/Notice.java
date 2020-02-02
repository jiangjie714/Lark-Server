package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: 工作台-公告栏
 * @author: dd
 * @since: 2019-06-07
 */
@Data
@Table(name = "PORTAL_NOTICE")
public class Notice implements Comparable<Notice>{
    @Column(name = "ID")
    @Id
    private String id;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "TOP")
    private String top;
    @Column(name = "ORG_CODE")
    private String orgCode;
    @Column(name = "SEND_TIME")
    private Date sendTime;
    @Column(name = "SECRET_LEVEL")
    private String secretLevel;

    @Override
    public int compareTo(Notice o) {
        return id.compareTo(o.id);
    }
}
