package com.github.hollykunge.security.admin.entity;

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
@Table(name = "ADMIN_RESOURCEROLEMAP_TASK")
public class AdminResourceRoleMapTask extends BaseEntity {

    @Column(name = "RESOURCE_ID")
    private String resourceId;

    @Column(name = "RESOURCE_TYPE")
    private String resourceType;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "DEFAULT_CHECK")
    private Short defaultCheck;

}