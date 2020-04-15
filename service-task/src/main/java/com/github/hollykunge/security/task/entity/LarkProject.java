package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_PROJECT")
public class LarkProject extends BaseEntity {

    @Column(name = "COVER")
    private String cover;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACCESS_CONTROL_TYPE")
    private String accessControlType;

    @Column(name = "WHITE_LIST")
    private String whiteList;

    @Column(name = "PROJECT_ORDER")
    private Integer projectOrder;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "TEMPLATE_CODE")
    private String templateCode;

    @Column(name = "SCHEDULE")
    private BigDecimal schedule;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "DELETED_TIME")
    private Date deletedTime;

    @Column(name = "PRIVATED")
    private Integer privated;

    @Column(name = "PREFIX")
    private String prefix;

    @Column(name = "OPEN_PREFIX")
    private Integer openPrefix;

    @Column(name = "ARCHIVE")
    private Integer archive;

    @Column(name = "ARCHIVE_TIME")
    private String archiveTime;

    @Column(name = "OPEN_BEGIN_TIME")
    private String openBeginTime;

    @Column(name = "OPEN_TASK_PRIVATED")
    private Integer openTaskPrivated;

    @Column(name = "TASK_BOARD_THEME")
    private String taskBoardTheme;

    @Column(name = "BEGIN_TIME")
    private Date beginTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "AUTO_UPDATE_SCHEDULE")
    private Integer autoUpdateSchedule;

}