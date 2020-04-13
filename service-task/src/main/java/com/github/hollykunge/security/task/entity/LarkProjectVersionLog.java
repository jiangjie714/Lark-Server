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
@Table(name = "LARK_PROJECT_VERSION_LOG")
public class LarkProjectVersionLog extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "MEMBER_CODE")
    private String memberCode;

    @Column(name = "TYPE")
    private String type;


    @Column(name = "SOURCE_CODE")
    private String sourceCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "FEATURES_CODE")
    private String featuresCode;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "REMARK")
    private String remark;

}