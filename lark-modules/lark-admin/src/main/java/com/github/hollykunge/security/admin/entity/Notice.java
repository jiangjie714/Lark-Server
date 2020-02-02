package com.github.hollykunge.security.admin.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "ADMIN_NOTICE")
//@JsonInclude(JsonInclude.Include.ALWAYS)
public class Notice extends BaseEntity {
    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;
    /**
     * 类型
     */
    @Column(name = "TYPE")
    private String type;
    /**
     * 内容
     */
    @Column(name = "CONTENT")
    private String content;
    /**
     * 附件
     */
    @Column(name = "ATTACHMENT")
    private String attachment;
    /**
     * 是否置顶
     */
    @Column(name = "IS_TOP")
    private String isTop;
    /**
     * 组织ID
     */
    @Column(name = "ORG_CODE")
    private String orgCode;
    /**
     * 组织名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;
    /**
     * 是否发送成功（1成功，0不成功）
     */
    @Column(name = "IS_SEND")
    private String isSend;

    @Column(name = "SECRET_LEVEL")
    private String secretLevel;
}