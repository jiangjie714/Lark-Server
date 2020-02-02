package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 卡片设置- 卡片信息主表
 * @author zhhongyu
 * @since 2019年06月1日
 */
@Data
@Table(name = "PORTAL_CARD_INFO")
public class CardInfo {
    @Column(name = "ID")
    @Id
    private String id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "URL")
    private String url;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DELETED")
    private String deleted;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "orgCode")
    private String orgCode;

    @Column(name = "orgName")
    private String orgName;

    @Column(name = "cardOrgUserStatus")
    private String  cardOrgUserStatus;
}
