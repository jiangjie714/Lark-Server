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
@Table(name = "LARK_SOURCE_LINK")
public class LarkSourceLink extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "SOURCE_TYPE")
    private String sourceType;

    @Column(name = "SOURCE_CODE")
    private String sourceCode;

    @Column(name = "LINK_TYPE")
    private String linkType;

    @Column(name = "LINK_CODE")
    private String linkCode;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "SORT")
    private Long sort;

}