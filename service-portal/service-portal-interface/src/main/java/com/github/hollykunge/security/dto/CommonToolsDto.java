package com.github.hollykunge.security.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class CommonToolsDto {
    private String id;
    private String uri;
    private String title;
    private String description;
    private String status;
    private String orgCode;
    private String orgName;
    /**
     * fansq 19-12-11 添加
     * 表明这个常用链接是否应用于常用链接所属部门的所有人员
     * 0 代表true
     * 1 代表false
     */
    private String  portalOrgUserStatus;
    /**
     * fansq 20-3-13
     * 添加 请求方式
     * GET POST
     */
    private String requestType;
    /**
     * fansq 20-3-13
     * 添加 请求参数
     * key:value
     * key:value
     */
    private Object requestParams;
}
