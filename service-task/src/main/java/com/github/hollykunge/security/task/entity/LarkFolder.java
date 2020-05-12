package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation 文件夹
 */
@Data
@Table(name = "LARK_FOLDER")
public class LarkFolder extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "TASK_CODE")
    private String taskCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "DELETED")
    private String deleted;

}