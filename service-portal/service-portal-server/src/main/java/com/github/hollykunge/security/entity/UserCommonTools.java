package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhhongyu
 * 用户设置常用工具类
 */
@Data
@Table(name = "PORTAL_USERCOMMONTOOLMAP")
public class UserCommonTools {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TOOL_ID")
    private String toolId;
}
