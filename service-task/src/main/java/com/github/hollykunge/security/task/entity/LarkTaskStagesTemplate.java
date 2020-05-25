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
@Table(name = "LARK_TASK_STAGES_TEMPLATE")
public class LarkTaskStagesTemplate extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROJECT_TEMPLATE_CODE")
    private String projectTemplateCode;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private String deleted;

}