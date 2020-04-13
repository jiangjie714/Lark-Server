package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_PROJECT_INFO")
public class LarkProjectInfo extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "SORT")
    private Long sort;

    @Column(name = "CODE")
    private String code;

}