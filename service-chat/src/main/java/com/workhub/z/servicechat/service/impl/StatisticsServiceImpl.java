package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.StatisticsGroupVo;
import com.workhub.z.servicechat.VO.StatisticsMsgVo;
import com.workhub.z.servicechat.dao.StatisticsDao;
import com.workhub.z.servicechat.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:13
 * @description:统计
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    StatisticsDao statisticsDao;

    /**
     * 群统计
     * @param groupName 群名称
     * @param isCross 跨场所
     * @return
     */
    @Override
    public TableResultResponse<StatisticsGroupVo> groupStatistics(int page, int size, String groupName, String isCross) {
        PageHelper.startPage(page, size);
        List<StatisticsGroupVo> dataList =this.statisticsDao.groupStatistics(groupName,isCross);
        PageInfo<StatisticsGroupVo> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<StatisticsGroupVo> res = new TableResultResponse<StatisticsGroupVo>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }

    /**
     * 消息统计
     * @return
     */
    @Override
    public StatisticsMsgVo msgStatistics() {
        return this.statisticsDao.msgStatistics();
    }
}