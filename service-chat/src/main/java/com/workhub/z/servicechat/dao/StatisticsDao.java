package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.StatisticsGroupVo;
import com.workhub.z.servicechat.VO.StatisticsMsgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统计
 */
public interface StatisticsDao {
    /**
     * 群消息统计
     * @param groupName 群名称
     * @param isCross 跨场所
     * @return
     */
    List<StatisticsGroupVo> groupStatistics(@Param("groupName") String groupName,@Param("isCross") String isCross);

    /**
     * 消息统计
     * @return
     */
    StatisticsMsgVo msgStatistics();
}
