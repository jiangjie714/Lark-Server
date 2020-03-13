package com.workhub.z.servicechat.controller.statistics;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.StatisticsGroupVo;
import com.workhub.z.servicechat.VO.StatisticsMsgVo;
import com.workhub.z.servicechat.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:13
 * @description: 统计
 */
@RestController
@RequestMapping("statistics")
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;

    /**
     * 群统计
     * @param page 页码
     * @param size 每页条数
     * @param groupName 群名称
     * @param isCross 跨场所属性 0 科室内 1 跨科室 2 跨场所
     * @return
     */
    @GetMapping("groupStatistics")
    public TableResultResponse<StatisticsGroupVo> groupStatistics(@RequestParam(value = "page",required=false,defaultValue = "1") int page,
                                                                  @RequestParam(value = "size",required=false,defaultValue = "10") int size,
                                                                  @RequestParam(value = "groupName",required=false,defaultValue = "") String groupName,
                                                                  @RequestParam(value = "isCross",required=false,defaultValue = "") String isCross){
        return this.statisticsService.groupStatistics(page,size,groupName,isCross);
    }

    /**
     * 群统计
     * @return
     */
    @GetMapping("msgStatistics")
    public ObjectRestResponse<StatisticsGroupVo> msgStatistics(){
        StatisticsMsgVo msgVo =  this.statisticsService.msgStatistics();
        return new ObjectRestResponse<>().rel(true).msg("200").data(msgVo);
    }
}