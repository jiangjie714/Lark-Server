package com.github.hollykunge.security.task.dto;

import com.github.hollykunge.security.task.entity.LarkProjectMember;
import lombok.Data;

import java.util.Date;

/**
 * @author fansq
 * @since 20-4-16
 * @deprecation 用于前端项目成员信息列表展示
 */
@Data
public class LarkProjectMemberDto extends LarkProjectMember {

    private String projectCode;

    private String memberCode;

    private Date joinTime;

    private Integer isOwner;

    private String authorize;

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

    private String projectUserId;
    private String projectUserName;
    private String projectUserOrgCode;
    private String projectUserPid;
    private String projectUserOrgCodeName;
    private String oEmail;
    private String avatar;

    private Boolean checkTrue;
    private Boolean checkFalse;
}
