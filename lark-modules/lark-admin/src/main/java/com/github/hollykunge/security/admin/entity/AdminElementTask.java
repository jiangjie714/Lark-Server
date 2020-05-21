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
@Table(name = "ADMIN_ELEMENT_TASK")
public class AdminElementTask extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "URI")
    private String uri;

    @Column(name = "MENU_ID")
    private String menuId;

    @Column(name = "PARENT_ID")
    private String parentId;

    @Column(name = "PATH")
    private String path;

    @Column(name = "METHOD")
    private String method;

    @Column(name = "DESCRIPTION")
    private String description;

}