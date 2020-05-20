package com.github.hollykunge.security.admin.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: zhhongyu
 * @description: 系统错误实体类
 * @since: Create in 13:48 2020/5/20
 */
@Data
@Table(name = "ADMIN_SYS_ERROR")
public class ErrorLogEntity extends BaseEntity {
    /**
     * 集群应用ip
     */
    @Column(name = "IP")
    private String ip;
    /**
     * 集群端口号
     */
    @Column(name = "PORT")
    private int port;
    /**
     * 错误信息
     */
    @Column(name = "ERRORMESSAGE")
    private String errorMessage;
    /**
     * 发生错误的java类
     */
    @Column(name = "ERRORCLASS")
    private String errorClass;
    /**
     * 发生错误的方法
     */
    @Column(name = "ERRORMETHOD")
    private String errorMethod;
    /**
     * 发生错误的代码行数
     */
    @Column(name = "ERRORLINE")
    private int errorLine;
}
