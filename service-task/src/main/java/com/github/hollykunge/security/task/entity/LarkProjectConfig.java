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
@Table(name = "LARK_PROJECT_CONFIG")
public class LarkProjectConfig extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

}