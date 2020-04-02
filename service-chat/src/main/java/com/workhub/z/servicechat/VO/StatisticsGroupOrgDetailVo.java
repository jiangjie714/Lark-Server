package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/3/31 13:19
 * @description: 群统计orgcode
 */
@Data
public class StatisticsGroupOrgDetailVo implements Serializable {

    private static final long serialVersionUID = 1193394788381257198L;
    /**组织编码*/
    private String orgCode;
    /**路径*/
    private String path;
    /**pid*/
    private String parentId;
}