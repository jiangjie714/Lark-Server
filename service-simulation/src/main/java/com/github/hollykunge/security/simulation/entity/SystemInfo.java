package com.github.hollykunge.security.simulation.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author jihang
 */

@Data
@Table(name = "SIMU_SYSTEM_INFO")
public class SystemInfo extends BaseEntity {


    /**
     * 创建者身份证号
     */
    @Column(name = "CREATOR_PID")
    private String creatorPId;

    /**
     * 创建者密级
     */
    @Column(name = "CREATOR_SECRETE_LEVEL")
    private String creatorSecreteLevel;

    /**
     * 系统名
     */
    @Column(name = "SYSTEM_NAME")
    private String systemName;

    /**
     * 系统密级
     */
    @Column(name = "LEVELS")
    private String levels;

    /**
     * 系统描述
     */
    @Column(name = "SYSTEM_DESCRIBE")
    private String systemDescribe;

    /**
     * 模型文件ID
     */
    @Column(name = "FILE_ID")
    private String fileId;

    /**
     * 系统状态
     */
    @Column(name = "STATE")
    private String state;

    /**
     * 参与者列表
     */
    @Transient
    private List<String> userList;

}
