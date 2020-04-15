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
@Table(name = "LARK_PROJECT_AUTH")
public class LarkProjectAuth extends BaseEntity {

    @Column(name = "TITLE")
    private String title;

    @Column(name = "STATUS")
    private Short status;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "DESCPTION")
    private String descption;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "CREATE_AT")
    private String createAt;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "IS_DEFAULT")
    private Short isDefault;

    @Column(name = "TYPE")
    private String type;

}