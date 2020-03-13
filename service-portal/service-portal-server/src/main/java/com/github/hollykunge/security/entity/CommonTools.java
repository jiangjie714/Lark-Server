package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;

/**
 * @description: 常用工具
 * @author: dd
 * @since: 2019-06-08
 */
@Data
@Table(name = "PORTAL_COMMONTOOLS")
public class CommonTools {
    @Column(name = "ID")
    @Id
    private String id;
    @Column(name = "URI")
    private String uri;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "ORG_CODE")
    private String orgCode;
    @Column(name = "ORG_NAME")
    private String orgName;
    /**
     * fansq 19-12-11 添加
     * 表明这个常用链接是否应用于常用链接所属部门的所有人员
     */
    @Column(name = "PORTAL_ORG_USER_STATUS")
    private String portalOrgUserStatus;
    /**
     * fansq 20-3-13
     * 添加 请求方式
     * GET POST
     */
    @Column(name = "REQUEST_TYPE")
    private String requestType;
    /**
     * fansq 20-3-13
     * 添加 请求参数
     * {key:value,key:value} 格式化后的json字符串
     */
    @Column(name = "REQUEST_PARAMS")
    private String requestParams;

}
