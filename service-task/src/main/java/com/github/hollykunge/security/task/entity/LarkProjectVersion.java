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
@Table(name = "LARK_PROJECT_VERSION")
public class LarkProjectVersion extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "PUBLISH_TIME")
    private Date publishTime;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "SCHEDULE")
    private BigDecimal schedule;

    @Column(name = "PLAN_PUBLISH_TIME")
    private Date planPublishTime;

    @Column(name = "FEATURES_CODE")
    private String featuresCode;

}