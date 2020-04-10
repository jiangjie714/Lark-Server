package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.model.StatisticsChartDto;
import com.workhub.z.servicechat.model.StatisticsGroupOrgDto;
import com.workhub.z.servicechat.model.StatisticsGroupUserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统计
 */
public interface StatisticsDao {
    /**
     * 群消息统计
     * @return
     */
    String groupInfStatistics(@Param("dateType") String dateType,@Param("xId") String xId);
    //List<StatisticsGroupVo> groupStatistics(@Param("groupName") String groupName,@Param("isCross") String isCross);

    /**
     * 消息统计
     * @return
     */
    String msgStatistics(@Param("dateType") String dateType,@Param("xId") String xId);
    //StatisticsMsgVo msgStatistics(@Param("dateType") String dateType);
    /**
     * 附件统计
     * @return
     */
    String fileStatistics(@Param("dateType") String dateType,@Param("xId") String xId);
    //StatisticsFileVo fileStatistics(@Param("dateType")String dateType);

    /**
     * 获取统计横坐标
     * @param orgCode 组织编码
     * @return
     */
    List<StatisticsChartDto> getXLabel(@Param("orgCode")String orgCode);
    List<StatisticsGroupUserDto> groupUserStatistics(@Param("groupId")String groupId);
    List<StatisticsGroupOrgDto> groupOrgStatistics(@Param("groupId")String groupId);
}
