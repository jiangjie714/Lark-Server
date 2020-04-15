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
@Table(name = "LARK_TASK_MEMBER")
public class LarkTaskMember extends BaseEntity {

    @Column(name = "TASK_CODE")
    private String taskCode;

    @Column(name = "IS_EXECUTOR")
    private String isExecutor;

    @Column(name = "MEMEBER_CODE")
    private String memeberCode;

    @Column(name = "JOIN_TIME")
    private Date joinTime;

    @Column(name = "IS_OWNER")
    private Integer isOwner;

}