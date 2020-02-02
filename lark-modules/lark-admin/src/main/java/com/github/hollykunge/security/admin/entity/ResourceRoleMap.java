package com.github.hollykunge.security.admin.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "ADMIN_RESOURCEROLEMAP")
public class ResourceRoleMap extends BaseEntity {

    @Column(name = "RESOURCE_ID")
    private String resourceId;

    @Column(name = "RESOURCE_TYPE")
    private String resourceType;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "DEFAULT_CHECKED")
    private Integer defaultCheck;

}