package com.github.hollykunge.security.gate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * log日志传输层
 * @author zhoyou
 */
@Data
@AllArgsConstructor
public class LogInfoDto implements Serializable{
    private String menu;

    private String opt;

    private String uri;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private String isSuccess;

    private String pid;

    private String optInfo;

    private String pathCode;
}
