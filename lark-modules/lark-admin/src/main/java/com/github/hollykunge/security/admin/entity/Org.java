package com.github.hollykunge.security.admin.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "ADMIN_ORG")
public class Org extends BaseEntity {
    /**
     * 组织名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;
    /**
     * 父级组织ID
     */
    @Column(name = "PARENT_ID")
    private String parentId;
    /**
     * 组织层级（几级单位）
     */
    @Column(name = "ORG_LEVEL")
    private Integer orgLevel;
    /**
     * 保密资格等级
     */
    @Column(name = "ORG_SECRET")
    private String orgSecret;
    /**
     * 组织别名
     */
    @Column(name = "EXTERNAL_NAME")
    private String externalName;
    /**
     * 排序号
     */
    @Column(name = "ORDER_ID")
    private Long orderId;
    /**
     * 是否删除，0为false，1为true
     */
    @Column(name = "DELETED")
    private String deleted;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 组织code
     */
    @Column(name = "ORG_CODE")
    private String orgCode;

    /**
     * 组织层级关系code
     */
    @Column(name = "PATH_CODE")
    private String pathCode;

    /**
     * 组织层级关系name
     */
    @Column(name = "PATH_NAME")
    private String pathName;
}