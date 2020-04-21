package com.github.hollykunge.security.task.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-4-20
 * @deprecation larkTask 前端数据返回实体类
 */
@Data
public class LarkTaskDto implements Serializable {

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

    private Integer pri;

    private String executeStatus;

    private String doneUser;

    private Date doneTime;

    private String assignTo;

    private Integer deleted;

    private String stagCode;

    private String taskTag;

    private Integer done;

    private Date beginTime;

    private Date endTime;

    private Date remindTime;

    private String pcode;

    private Integer sort;

    private Integer taskLike;

    private Integer star;

    private Date deletedTime;

    private Integer taskPrivate;

    private String idNum;

    private BigDecimal schedule;

    private String versionCode;

    private String featuresCode;

    private Integer workTime;

    private String status;

    private String description;

    private String path;

    private String projectName;

    private String userName;

    private String avatar;

    private TaskNum taskNum;

    private List<LarkTaskDto> larkTaskList;

    private List<LarkTaskTagDto> larkTaskTagList;
}
