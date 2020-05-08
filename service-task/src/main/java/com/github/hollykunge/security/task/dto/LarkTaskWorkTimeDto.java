package com.github.hollykunge.security.task.dto;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fansq
 * @since 20-4-30
 * @deprecation 工作时 表
 */
@Data
public class LarkTaskWorkTimeDto implements Serializable {

    private String id;

    private String taskCode;

    private String memberCode;

    private String content;

    private Date beginTime;

    private Integer num;

    private String code;

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
}
