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
@Table(name = "LARK_TASK_WORKFLOW_RULE")
public class LarkTaskWorkflowRule extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "TYPE")
    private Short type;

    @Column(name = "OBJECT_CODE")
    private String objectCode;

    @Column(name = "ACTION")
    private Short action;

    @Column(name = "WORKFLOW_CODE")
    private String workflowCode;

    @Column(name = "SORT")
    private Short sort;

}