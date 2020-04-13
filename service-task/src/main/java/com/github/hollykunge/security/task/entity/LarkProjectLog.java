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
@Table(name = "LARK_PROJECT_LOG")
public class LarkProjectLog extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "MEMBER_CODE")
    private String memberCode;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SOURCE_CODE")
    private String sourceCode;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "TO_MEMBER_CODE")
    private String toMemberCode;

    @Column(name = "IS_COMMENT")
    private Short isComment;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "IS_ROBOT")
    private Short isRobot;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "REMARK")
    private String remark;

}