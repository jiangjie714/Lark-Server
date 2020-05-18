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
@Table(name = "LARK_PROJECT_MEMBER")
public class LarkProjectMember extends BaseEntity {

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "MEMBER_CODE")
    private String memberCode;

    @Column(name = "JOIN_TIME")
    private Date joinTime;

    @Column(name = "IS_OWNER")
    private Long isOwner;

    @Column(name = "AUTHORIZE")
    private String authorize;

}