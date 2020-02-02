package com.github.hollykunge.security.admin.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "ADMIN_ROLE")
public class Role extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PARENT_ID")
    private String parentId;

    @Column(name = "PATH")
    private String path;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "GROUP_TYPE")
    private Long groupType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORG_ID")
    private String orgId;

    @Column(name = "ORG_NAME")
    private String orgName;
}