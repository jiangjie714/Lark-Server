package com.github.hollykunge.security.task.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-4-30
 * @deprecation  taskMember dto
 */
@Data
public class LarkTaskMemberDto implements Serializable {

    //任务信息
    private String taskCode;
    private Integer isExecutor;
    private String memeberCode;
    private Date joinTime;
    private Integer isOwner;

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

    //人员信息
    private String avatar;
    private String orgCode;
    private String orgName;
    private String pid;
    private String userName;

    //子任务信息
    private List<LarkTaskMemberDto> larkTaskMemberDtos;
}
