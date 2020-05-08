package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_FILE")
public class LarkFile extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "PATH_NAME")
    private String pathName;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "EXTENSION")
    private String extension;

    @Column(name = "FILE_SIZE")
    private Integer fileSize;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "TASK_CODE")
    private String taskCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "DOWNLOADS")
    private Integer downloads;

    @Column(name = "EXTRA")
    private String extra;

    @Column(name = "DELETED")
    private String deleted;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "DELETED_TIME")
    private String deletedTime;
}