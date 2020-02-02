package com.github.hollykunge.security.vo;

import lombok.Data;

/**
 * @author: zhhongyu
 * @description: 文件信息前端页面展示
 * @since: Create in 10:20 2019/8/28
 */
@Data
public class FileInfoVO {
    private String id;

    private String fileName;
    /**
     * 文件后缀名
     */
    private String fileExt;
    /**
     * 附件类型（text、img、doc）
     */
    private String fileType;
    /**
     * 文件密级
     */
    private String levels;

    /**
     * 文件大小
     */
    private Double fileSize;

    /**
     * 附件转码为可读取的路径
     */
    private String readPath;
    /**
     * 数据有效状态（1为有效，0为无效）
     */
    private String status;
    /**
     * 加密类型
     */
    private String sensitiveType;
    /**
     * 文件服务器路径
     */
    private String path;
}
