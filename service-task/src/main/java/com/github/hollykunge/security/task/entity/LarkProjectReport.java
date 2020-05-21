package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_PROJECT_REPORT")
public class LarkProjectReport extends BaseEntity {

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "REPORT_DATE")
    private Date reportDate;

    @Column(name = "CONTENT")
    private String content;

}