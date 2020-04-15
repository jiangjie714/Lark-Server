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
@Table(name = "LARK_TASK_TO_TAG")
public class LarkTaskToTag extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "TASK_CODE")
    private String taskCode;

    @Column(name = "TAG_CODE")
    private String tagCode;

}