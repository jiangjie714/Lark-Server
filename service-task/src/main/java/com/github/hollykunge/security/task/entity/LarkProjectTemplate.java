package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_PROJECT_TEMPLATE")
public class LarkProjectTemplate extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "CODE")
    private String code;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "COVER")
    private String cover;

    @Column(name = "MEMBER_CODE")
    private String memberCode;

    @Column(name = "IS_SYSTEM")
    private Integer isSystem;

    @Column(name = "DESCRIPTION")
    private String description;

}