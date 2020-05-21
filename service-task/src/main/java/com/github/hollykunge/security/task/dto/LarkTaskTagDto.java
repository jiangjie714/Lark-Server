package com.github.hollykunge.security.task.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LarkTaskTagDto implements Serializable {

    private String id;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private Date updTime;

    private String updUser;

    private String updName;

    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String code;

    private String projectCode;

    private String name;

    private String color;
}
