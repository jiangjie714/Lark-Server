package com.github.hollykunge.security.task.dto;

import com.github.hollykunge.security.common.entity.BaseEntity;
import com.github.hollykunge.security.task.entity.LarkProject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author  fansq
 * @since 20-4-15
 * 用于给前端返回 项目具体信息
 * 增加项目拥有者 信息
 * @deprecation
 */
@Data
public class LarkProjectDto implements Serializable {

    private String cover;

    private String name;

    private String code;

    private String description;

    private String accessControlType;

    private String whiteList;

    private Integer projectOrder;

    private Integer deleted;

    private String templateCode;

    private BigDecimal schedule;

    private String organizationCode;

    private Date deletedTime;

    private Integer privated;

    private String prefix;

    private Integer openPrefix;

    private Integer archive;

    private Date archiveTime;

    private String openBeginTime;

    private Integer openTaskPrivated;

    private String taskBoardTheme;

    private Date beginTime;

    private Date endTime;

    private Integer autoUpdateSchedule;

    private Integer secretLevel;

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

    //projectUserId -> avatar  初始化项目信息时 存储用户数据使用
    private String projectUserId;
    private String projectUserName;
    private String projectUserOrgCode;
    private String projectUserPid;
    private String projectUserOrgCodeName;
    private String oEmail;
    private Integer isOwner;
    private String avatar;

    //elementList -> roleName 初始化项目信息时 存储权限资源使用
    private List<LarkTaskElement> elementList;
    private String roleId;
    private String roleCode;
    private String roleName;
}