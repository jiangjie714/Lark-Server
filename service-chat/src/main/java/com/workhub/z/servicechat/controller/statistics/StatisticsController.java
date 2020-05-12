package com.workhub.z.servicechat.controller.statistics;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.StatisticsChartDataVo;
import com.workhub.z.servicechat.VO.StatisticsGroupOrgVo;
import com.workhub.z.servicechat.VO.StatisticsGroupUserVo;
import com.workhub.z.servicechat.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:13
 * @description: 统计
 */
//todo 统计数据，分支切换后可以删除
@RestController
@RequestMapping("statistics")
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;
    /**
     * 群统计
     * @param orgCode 横坐标父节点
     * @param dateType 日期类型
     * @return
     */
    @GetMapping("groupInfStatistics")
    public ObjectRestResponse groupStatistics(@RequestParam("orgCode")String orgCode,@RequestParam(name = "date",required = false)String dateType){
        List<StatisticsChartDataVo> dataList = this.statisticsService.groupInfStatistics(dateType,orgCode);
        return new ObjectRestResponse<>().rel(true).msg("200").data(dataList);
    }
    /*@GetMapping("groupStatistics")
    public  TableResultResponse<StatisticsGroupVo> groupStatistics(@RequestParam(value = "page",required=false,defaultValue = "1") int page,
                                                                   @RequestParam(value = "size",required=false,defaultValue = "10") int size,
                                                                   @RequestParam(value = "groupName",required=false,defaultValue = "") String groupName,
                                                                   @RequestParam(value = "isCross",required=false,defaultValue = "") String isCross){
        return this.statisticsService.groupStatistics(page,size,groupName,isCross);
    }*/

    /**
     * 消息统计
     * @param orgCode 横坐标父节点
     * @param dateType 日期类型
     * @return
     */
    @GetMapping("msgStatistics")
    public ObjectRestResponse msgStatistics(@RequestParam("orgCode")String orgCode,@RequestParam(name = "date",required = false)String dateType){
        List<StatisticsChartDataVo> dataList = this.statisticsService.msgStatistics(dateType,orgCode);
        return new ObjectRestResponse<>().rel(true).msg("200").data(dataList);
    }
   /* @GetMapping("msgStatistics")
    public ObjectRestResponse<StatisticsMsgVo> msgStatistics(@RequestParam("dateType")String dateType){
        StatisticsMsgVo msgVo =  this.statisticsService.msgStatistics(dateType);
        return new ObjectRestResponse<>().rel(true).msg("200").data(msgVo);
    }*/

    /**
     * 附件统计
     * @param orgCode 横坐标父节点
     * @param dateType 日期类型
     * @return
     */
    @GetMapping("fileStatistics")
    public ObjectRestResponse fileStatistics(@RequestParam("orgCode")String orgCode,@RequestParam(name = "date",required = false)String dateType){
        List<StatisticsChartDataVo> dataList = this.statisticsService.fileStatistics(dateType,orgCode);
        return new ObjectRestResponse<>().rel(true).msg("200").data(dataList);
    }
    /*public ObjectRestResponse<StatisticsFileVo> fileStatistics(@RequestParam("dateType")String dateType){
        StatisticsFileVo fileVo =  this.statisticsService.fileStatistics(dateType);
        return new ObjectRestResponse<>().rel(true).msg("200").data(fileVo);
    }*/
    /**
     * 群成员统计
     * @param groupId 群id
     * @return
     */
    @GetMapping("groupUserStatistics")
    public ObjectRestResponse groupUserStatistics(@RequestParam(name = "groupId",required = false)String groupId){
        List<StatisticsGroupUserVo> dataList = this.statisticsService.groupUserStatistics(groupId);
        return new ObjectRestResponse<>().rel(true).msg("200").data(dataList);
    }
    /**
     * 群org统计
     * @param groupId  群id
     * @return
     */
    @GetMapping("groupOrgStatistics")
    public ObjectRestResponse groupOrgStatistics(@RequestParam(name = "groupId",required = false)String groupId){
        List<StatisticsGroupOrgVo> dataList = this.statisticsService.groupOrgStatistics(groupId);
        return new ObjectRestResponse<>().rel(true).msg("200").data(dataList);
    }

    /**
     * 获取参加的所有群组信息
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("getGroupAndMeetIdByUserId")
    public ObjectRestResponse getGroupAndMeetIdByUserId(String userId){
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        List<String> ids = this.statisticsService.getGroupAndMeetIdByUserId(userId);
        objectRestResponse.msg("200");
        objectRestResponse.data(ids);
        return objectRestResponse;
    }
}