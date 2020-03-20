package com.github.hollykunge.security.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "PORTAL_SIMULATION")
public class Simulation extends BaseEntity {

    /**
     * 系统名称
     */
    @Column(name = "SYSTEM_NAME")
    private String systemName;

    /**
     * 系统id
     */
    @Column(name = "SYSTEM_ID")
    private String systemId;

    /**
     * 系统描述
     */
    @Column(name = "SYSTEM_DESCRIBE")
    private String systemDescribe;

    /**
     * 用户名
     */
    @Column(name = "USER_ID")
    private String userId;
}
