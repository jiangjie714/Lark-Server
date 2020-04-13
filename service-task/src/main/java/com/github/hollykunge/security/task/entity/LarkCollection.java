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
@Table(name = "LARK_COLLECTION")
public class LarkCollection  extends BaseEntity {


    @Column(name = "CODE")
    private String code;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SOURCE_CODE")
    private String sourceCode;

    @Column(name = "MEMBER_CODE")
    private String memberCode;








}