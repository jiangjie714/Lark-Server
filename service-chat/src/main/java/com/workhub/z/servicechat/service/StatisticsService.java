package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.StatisticsGroupVo;
import com.workhub.z.servicechat.VO.StatisticsMsgVo;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:13
 * @description:统计
 */

public interface StatisticsService {
    /**
     * 群统计
     * @param groupName 群名称
     * @param isCross 跨场所
     * @return
     */
     TableResultResponse<StatisticsGroupVo> groupStatistics(int page, int size, String groupName, String isCross) ;

    /**
     * 消息统计
     * @return
     */
     StatisticsMsgVo msgStatistics() ;
}