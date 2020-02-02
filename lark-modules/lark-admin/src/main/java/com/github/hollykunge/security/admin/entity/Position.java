package com.github.hollykunge.security.admin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ADMIN_POSITION")
@Data
public class Position {
    //ID
    @Id
    @Column(name = "ID")
    private String id;
    //岗位名称
    @Column(name = "NAME")
    private String name;
    //排序
    @Column(name = "SORT")
    private Long sort;
    //岗位描述
    @Column(name = "DESCRIPTION")
    private String description;
    //岗位类型
    @Column(name = "TYPE")
    private String type;

    @Column(name = "CRT_TIME")
    @JSONField( format ="yyyy-MM-dd HH:mm:ss")
    private Date crtTime;

    @Column(name = "CRT_USER")
    private String crtUser;

    @Column(name = "CRT_NAME")
    private String crtName;

    @Column(name = "CRT_HOST")
    private String crtHost;

    @Column(name = "UPD_TIME")
    @JsonFormat( pattern ="yyyy-MM-dd HH:mm:ss")
    private Date updTime;

    @Column(name = "UPD_USER")
    private String updUser;

    @Column(name = "UPD_NAME")
    private String updName;

    @Column(name = "UPD_HOST")
    private String updHost;

    @Column(name = "ATTR1")
    private String attr1;

    @Column(name = "ATTR2")
    private String attr2;

    @Column(name = "ATTR3")
    private String attr3;

    @Column(name = "ATTR4")
    private String attr4;
    //权限
    @Column(name = "PERMISSION")
    private Integer permission;
}