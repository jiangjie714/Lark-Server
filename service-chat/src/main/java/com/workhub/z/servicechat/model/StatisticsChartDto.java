package com.workhub.z.servicechat.model;

import lombok.Data;

/**
 * @auther: zhuqz
 * @date: 2020/3/26 16:22
 * @description: 统计信息 横坐标 和 对应数据
 */
@Data
public class StatisticsChartDto {
    private String orgCode;//组织编码
    private String id;//组织id
    private String orgName;//组织名称（横坐标名称）
    private String cnt;//个数
}