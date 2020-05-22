package com.github.hollykunge.security.common.feign;

import lombok.Data;
import lombok.ToString;

/**
 * @author: zhhongyu
 * @description: 系统级错误消息实体类
 * @since: Create in 9:04 2020/5/20
 */
@Data
@ToString
public class ErrorMessageEntity {
    /**
     * 集群应用ip
     */
    private String ip;
    /**
     * 集群端口号
     */
    private int port;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 发生错误的java类
     */
    private String errorClass;
    /**
     * 发生错误的方法
     */
    private String errorMethod;
    /**
     * 发生错误的代码行数
     */
    private int errorLine;
}
