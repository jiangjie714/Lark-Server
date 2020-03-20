package com.github.hollykunge.security.simulation.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author jihang
 */

@Data
@Table(name = "SIMU_SYSTEM_USER_MAP")
public class SystemUserMap extends BaseEntity {

    /**
     * 系统id
     */
    @Column(name = "SYSTEM_ID")
    private String systemId;

    /**
     * 用户id
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 是否创建者,0/1
     */
    @Column(name = "IS_CREATOR")
    private String isCreator;
}
