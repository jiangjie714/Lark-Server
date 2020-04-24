package com.github.hollykunge.security.task.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class LarkProjectTemplateDto implements Serializable {

    private String name;
    private Integer sort;
    private String code;
    private String organizationCode;
    private String cover;
    private String memberCode;
    private Integer isSystem;
    private String description;
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
    private List<LarkTaskStagesDto> larkTaskStagesDtos;
}
