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
@Table(name = "LARK_PROJECT_FEATURES")
public class LarkProjectFeatures extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

}