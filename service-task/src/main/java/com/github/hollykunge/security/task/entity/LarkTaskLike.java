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
@Table(name = "LARK_TASK_LIKE")
public class LarkTaskLike extends BaseEntity {

    @Column(name = "TASK_CODE")
    private String taskCode;

    @Column(name = "MEMBER_CODE")
    private String memberCode;

}