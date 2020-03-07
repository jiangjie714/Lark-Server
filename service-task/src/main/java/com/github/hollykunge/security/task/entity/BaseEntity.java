package com.github.hollykunge.security.task.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @description: 实体基类
 * @author: fansq
 * @since: 20-3-6
 */
@Data
public class BaseEntity {

    @Id
    private String id;

    @Field("CRT_TIME")
    private Date crtTime;

    @Field("CRT_USER")
    private String crtUser;

    @Field("CRT_NAME")
    private String crtName;

    @Field("CRT_HOST")
    private String crtHost;

    @Field("UPD_TIME")
    private Date updTime;

    @Field("UPD_USER")
    private String updUser;

    @Field("UPD_NAME")
    private String updName;

    @Field("UPD_HOST")
    private String updHost;

    @Field("ATTR1")
    private String attr1;

    @Field("ATTR2")
    private String attr2;

    @Field("ATTR3")
    private String attr3;

    @Field("ATTR4")
    private String attr4;
}
