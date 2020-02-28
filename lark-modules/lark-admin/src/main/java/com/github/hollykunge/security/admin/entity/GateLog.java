package com.github.hollykunge.security.admin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Table(name = "ADMIN_GATELOG")
public class GateLog {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "MENU")
    private String menu;

    @Column(name = "OPT")
    private String opt;

    @Column(name = "URI")
    private String uri;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Column(name = "CRT_TIME")
    private Date crtTime;

    @Column(name = "CRT_USER")
    private String crtUser;

    @Column(name = "CRT_NAME")
    private String crtName;

    @Column(name = "CRT_HOST")
    private String crtHost;

    @Column(name = "IS_SUCCESS")
    private String isSuccess;

    @Column(name = "P_ID")
    private String pid;

    @Column(name = "OPT_INFO")
    private String optInfo;

    @Column(name = "ORG_CODE")
    private String orgCode;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "PATH_CODE")
    private String pathCode;

    @Column(name = "PATH_NAME")
    private String pathName;

}
