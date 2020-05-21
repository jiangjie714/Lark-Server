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
@Table(name = "LARK_TASK_TAG")
public class LarkTaskTag extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COLOR")
    private String color;

}