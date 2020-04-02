package com.github.hollykunge.security.admin.api.authority;

import lombok.Data;

/**
 * @auther: zhuqz
 * @date: 2020/3/29 09:50
 * @description: 群成员统计
 */
@Data
public class StatisticsGroupUserDto {

    /**群id*/
    private String groupId;
    /**群名称*/
    private String groupName;
    /**人员姓名*/
    private String name;
    /**人员orgCode*/
    private String orgCode;
    /**人员组织路径*/
    private String path;
}