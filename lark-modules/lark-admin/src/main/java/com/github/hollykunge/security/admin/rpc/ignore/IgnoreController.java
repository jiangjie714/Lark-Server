package com.github.hollykunge.security.admin.rpc.ignore;

import com.github.hollykunge.security.admin.api.authority.*;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.rpc.ignore.service.IgnoreService;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author fansq  ignore
 * @deprecation 不走网关并提供给其他服务的接口  ignoreController
 * @since 20-3-23
 */
@RestController
@RequestMapping("/ignore")
@Slf4j
public class IgnoreController {

    @Autowired
    private GateLogBiz gateLogBiz;

    /**
     * 用于门户统计页面数据初始化
     *
     * @return
     */
    @RequestMapping(value = "/statistics/all", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<PortalStatistics> statisticsAll() throws Exception {
        PortalStatistics portalStatistics = new PortalStatistics();
        //获取总访问量 所有请求之和
        int totalAccess = gateLogBiz.getTotalAccess();
        portalStatistics.setTotalAccess(new Long(totalAccess));
        //获取日访问量  昨天的请求综合
        int dayAccess = gateLogBiz.getAccess(CommonConstants.YESTERDAY);
        portalStatistics.setDayAccess(new Long(dayAccess));
        //获取今年总访问量 今年所有请求之和
        int yearTotalAccess = gateLogBiz.getAccess(CommonConstants.THIS_YEAR);
        //获取去年总访问量  去年所有请求之和
        int lastYearTotalAccess = gateLogBiz.getAccess(CommonConstants.LAST_YEAR);
        //如果增长率为负 修改为0
        if (yearTotalAccess < lastYearTotalAccess) {
            portalStatistics.setTotalRate(0.00);
        } else {
            //如果去年增长量为0  被除数修改为1
            if (lastYearTotalAccess == 0) {
                lastYearTotalAccess = 1;
            }
            //总增长率（今年的请求之和-去年请求之和）/去年请求之和
            double accessDiff = new Double(totalAccess - lastYearTotalAccess).doubleValue();
            double lastYearTotalAccessDouble = new Double(lastYearTotalAccess).doubleValue();
            String result = new DecimalFormat("0.00").format(accessDiff / lastYearTotalAccessDouble);
            portalStatistics.setTotalRate(Math.ceil(Double.parseDouble(result)));
        }
        // 获取日访问量，前天的请求综合
        int dayAccessTwo = gateLogBiz.getAccess(CommonConstants.BEFOR_YESTERDAY);
        if (dayAccess < dayAccessTwo) {
            portalStatistics.setDayRate(0.00);
        } else {
            if (dayAccessTwo == 0) {
                dayAccessTwo = 1;
            }
            //日增长率（昨天请求之和-前天请求之和）/前天请求之和
            double dayAccessDiff = new Double(dayAccess - dayAccessTwo).doubleValue();
            double dayAccessTwoDouble = new Double(dayAccessTwo).doubleValue();
            String resultDay = new DecimalFormat("0.00").format(dayAccessDiff / dayAccessTwoDouble);
            portalStatistics.setDayRate(Math.ceil(Double.parseDouble(resultDay)));
        }
        portalStatistics.setAccessNums(gateLogBiz.accessNums("0010", CommonConstants.BEN_YUE));
        portalStatistics.setMessageNums(gateLogBiz.messageNums("0010", CommonConstants.BEN_YUE));
        portalStatistics.setFileNums(gateLogBiz.fileNums("0010", CommonConstants.BEN_YUE));
        portalStatistics.setGroupNums(gateLogBiz.groupNums("0010", CommonConstants.BEN_YUE));
        //饼图
        portalStatistics.setSourceOrg(gateLogBiz.getSourceOrg("0010", CommonConstants.BEN_YUE));
        //散点图  todo 暂时不需要了
        return new ObjectRestResponse<>().data(portalStatistics).msg("查询成功！");
    }

    /**
     * 门户统计页面 具体数据获取 例如本单位 本部门 按时间区分 等
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/statistics/bar", method = RequestMethod.GET)
    public ObjectRestResponse statisticsBar(@RequestParam Map<String, Object> map) throws Exception {
        PortalStatistics portalStatistics = new PortalStatistics();
        if (map.isEmpty()) {
            throw new BaseException("参数不可为空！");
        }
        Object dateRange = map.get("dateRange");
        if (dateRange == "" || dateRange == null) {
            throw new BaseException("时间范围不可为空！");
        }
        Object unitRange = map.get("unitRange");
        if (dateRange == "" || dateRange == null) {
            throw new BaseException("单位组织范围不可为空！");
        }
        //柱状图  访问量
        List<AccessNum> accessNums = gateLogBiz.accessNums(unitRange.toString(), dateRange.toString());
        portalStatistics.setAccessNums(accessNums);
        //柱状图  消息
        portalStatistics.setMessageNums(gateLogBiz.messageNums(unitRange.toString(), dateRange.toString()));
        //柱状图   文件数
        portalStatistics.setFileNums(gateLogBiz.fileNums(unitRange.toString(), dateRange.toString()));
        //柱状图   群组数
        portalStatistics.setGroupNums(gateLogBiz.groupNums(unitRange.toString(), dateRange.toString()));
        //饼图
        portalStatistics.setSourceOrg(gateLogBiz.getSourceOrg(unitRange.toString(), dateRange.toString()));
        return new ObjectRestResponse<>().data(portalStatistics).msg("查询成功！");
    }
}
