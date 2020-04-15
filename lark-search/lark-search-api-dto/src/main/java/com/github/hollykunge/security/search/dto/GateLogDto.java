package com.github.hollykunge.security.search.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author: zhhongyu
 * @description: 网关日志dto
 * @since: Create in 15:20 2020/2/27
 */
@Data
public class GateLogDto {
    private String id;
    private String crtName;
    private String opt;
    private String isSuccess;
    private String mongodbCollection;
    private String menu;
    private String ksendFlag;
    private String uri;
    private String optInfo;
    private Date crtTime;
    private String pid;
    private String crtUser;
    private String crtHost;
    private String pathCode;
}
