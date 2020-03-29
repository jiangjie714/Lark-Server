package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.VO.StatisticsChartDataVo;
import com.workhub.z.servicechat.VO.StatisticsGroupUserVo;

import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:13
 * @description:统计
 */

public interface StatisticsService {
    /**
     * 群统计
     * @return
     */
    List<StatisticsChartDataVo> groupInfStatistics(String dateType, String orgCode) ;
     //TableResultResponse<StatisticsGroupVo> groupStatistics(int page,int size,String groupName, String isCross) ;

    /**
     * 消息统计
     * @return
     */
    List<StatisticsChartDataVo> msgStatistics(String dateType, String orgCode) ;
     //StatisticsMsgVo msgStatistics(String dateType) ;
    /**
     * 附件统计
     * @return
     */
    List<StatisticsChartDataVo> fileStatistics(String dateType, String orgCode) ;
    //StatisticsFileVo fileStatistics(String dateType) ;
    /**
     * 群成员统计
     * @return
     */
    List<StatisticsGroupUserVo> groupUserStatistics(String groupId);
}