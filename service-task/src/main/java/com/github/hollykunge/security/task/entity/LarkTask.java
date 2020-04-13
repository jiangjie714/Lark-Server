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
@Table(name = "LARK_TASK")
public class LarkTask extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRI")
    private Short pri;

    @Column(name = "EXECUTE_STATUS")
    private String executeStatus;

    @Column(name = "DONE_USER")
    private String doneUser;

    @Column(name = "DONE_TIME")
    private Date doneTime;

    @Column(name = "ASSIGN_TO")
    private String assignTo;

    @Column(name = "DELETED")
    private Short deleted;

    @Column(name = "STAG_CODE")
    private String stagCode;

    @Column(name = "TASK_TAG")
    private String taskTag;

    @Column(name = "DONE")
    private Short done;

    @Column(name = "BEGIN_TIME")
    private Date beginTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "REMIND_TIME")
    private Date remindTime;

    @Column(name = "PCODE")
    private String pcode;

    @Column(name = "SORT")
    private Long sort;

    @Column(name = "TASK_LIKE")
    private Integer taskLike;

    @Column(name = "STAR")
    private Integer star;

    @Column(name = "DELETED_TIME")
    private String deletedTime;

    @Column(name = "TASK_PRIVATE")
    private Short taskPrivate;

    @Column(name = "ID_NUM")
    private Integer idNum;

    @Column(name = "SCHEDULE")
    private Short schedule;

    @Column(name = "VERSION_CODE")
    private String versionCode;

    @Column(name = "FEATURES_CODE")
    private String featuresCode;

    @Column(name = "WORK_TIME")
    private Integer workTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PATH")
    private String path;

}