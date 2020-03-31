package com.workhub.z.servicechat.model;

import lombok.Data;

/**
 * @auther: zhuqz
 * @date: 2020/3/31 13:18
 * @description: 群org统计
 */
@Data
public class StatisticsGroupOrgDto {
    /**群id*/
    private String groupId;
    /**群名称*/
    private String groupName;
    /**orgCode*/
    private String orgCode;
    /**组织路径*/
    private String path;
    /**pid*/
    private String pid;
}