package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/3/27 09:39
 * @description: 统计数据展示
 */
@Data
public class StatisticsChartDataVo implements Serializable {
    private static final long serialVersionUID = -8117962351974608984L;
    /**
     * 单位名称  部门名称
     */
    private String x;
    /**
     * 登录次数  访问量
     */
    private Long y;
}