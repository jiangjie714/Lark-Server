package com.github.hollykunge.security.task.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fansq
 * @since 20-4-21
 * @deprecation 任务总数 以及 已完成数
 */
@Data
public class TaskNum implements Serializable {

    private String total;
    private String completed;
}
