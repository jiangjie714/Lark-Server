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
@Table(name = "LARK_TASK_STAGES")
public class LarkTaskStages extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "DESCRPTION")
    private String descrption;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DELETED")
    private Integer deleted;

}