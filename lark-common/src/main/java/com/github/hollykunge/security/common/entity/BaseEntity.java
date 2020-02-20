package com.github.hollykunge.security.common.entity;
import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @description: 实体基类
 * @author: dd
 * @since: 2019-05-16
 */
@Data
public class BaseEntity {

    @ExcelIgnore
    @Id
    @Column(name = "Id")
    private String id;

    @ExcelIgnore
    @Column(name = "CRT_TIME")
    private Date crtTime;

    @ExcelIgnore
    @Column(name = "CRT_USER")
    private String crtUser;

    @ExcelIgnore
    @Column(name = "CRT_NAME")
    private String crtName;

    @ExcelIgnore
    @Column(name = "CRT_HOST")
    private String crtHost;

    @ExcelIgnore
    @Column(name = "UPD_TIME")
    private Date updTime;

    @ExcelIgnore
    @Column(name = "UPD_USER")
    private String updUser;

    @ExcelIgnore
    @Column(name = "UPD_NAME")
    private String updName;

    @ExcelIgnore
    @Column(name = "UPD_HOST")
    private String updHost;

    @ExcelIgnore
    @Column(name = "ATTR1")
    private String attr1;

    @ExcelIgnore
    @Column(name = "ATTR2")
    private String attr2;

    @ExcelIgnore
    @Column(name = "ATTR3")
    private String attr3;

    @ExcelIgnore
    @Column(name = "ATTR4")
    private String attr4;
}
