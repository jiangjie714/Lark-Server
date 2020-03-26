package com.github.hollykunge.security.admin.rpc;

import com.github.hollykunge.security.admin.api.authority.AccessNum;
import com.github.hollykunge.security.admin.api.authority.PortalStatistics;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.entity.GateLog;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.rpc.service.PermissionService;
import com.github.hollykunge.security.admin.util.DateUntil;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fansq
 * @since 20-3-23
 * @deprecation 门户统计功能
 */
@RestController
@RequestMapping("/statistics")
@Slf4j
public class PortalStatisticsController  {

    @Autowired
    private GateLogBiz gateLogBiz;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private OrgBiz orgBiz;

    @Value("${auth.user.token-header}")
    private String headerName;

    @Autowired
    private PermissionService permissionService;
    /**
     * 用于门户统计页面数据初始化
     * @param request
     * @return
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<PortalStatistics> statisticsAll(HttpServletRequest request) throws Exception{
        PortalStatistics portalStatistics = new PortalStatistics();
        int totalAccess = getTotalAccess();
        portalStatistics.setTotalAccess(new Long(totalAccess));
        int dayAccess = getDayAccess(-1);
        portalStatistics.setDayAccess(new Long(dayAccess));
        int  lastYearTotalAccess = getLastYearTotalAccess();
        if(lastYearTotalAccess==0){
            lastYearTotalAccess=1;
        }
        //总增长率（今年的请求之和-去年请求之和）/去年请求之和
        double accessDiff = new Double(totalAccess-lastYearTotalAccess).doubleValue();
        double  lastYearTotalAccessDouble = new Double(lastYearTotalAccess).doubleValue();
        String result = new DecimalFormat("0.0000").format(accessDiff/lastYearTotalAccess);
        portalStatistics.setTotalRate(Math.ceil(Double.parseDouble(result)));
        int dayAccessTwo = getDayAccess(-2);
        if(dayAccessTwo==0){
            dayAccessTwo=1;
        }
        //日增长率（昨天请求之和-前天请求之和）/前天请求之和
        double dayAccessDiff = new Double(dayAccess-dayAccessTwo).doubleValue();
        double  dayAccessTwoDouble = new Double(dayAccessTwo).doubleValue();
        String resultDay = new DecimalFormat("0.0000").format(dayAccessDiff/dayAccessTwoDouble);
        portalStatistics.setDayRate(Math.ceil(Double.parseDouble(resultDay)));
        String userId = request.getHeader("userId");
        List<AccessNum> accessNumList = accessNums("0010",null);
        portalStatistics.setAccessNums(accessNumList);
        return new ObjectRestResponse<>().data(portalStatistics).msg("查询成功！");
    }

    /**
     * 门户统计页面 具体数据获取 例如本单位 本部门 按时间区分 等
     * @param map
     * @return
     */
    @RequestMapping(value = "/bar",method = RequestMethod.GET)
    public ObjectRestResponse statisticsBar(@RequestParam Map<String, Object> map) throws Exception{
        PortalStatistics portalStatistics = new PortalStatistics();
        if(map.isEmpty()){
            throw new BaseException("参数不可为空！");
        }
        Object dateRange = map.get("dateRange");
        if(dateRange==""||dateRange==null){
            throw new BaseException("时间范围不可为空！");
        }
        Object unitRange = map.get("unitRange");
        if(dateRange==""||dateRange==null){
            throw new BaseException("单位组织范围不可为空！");
        }
        List<AccessNum> accessNums = accessNums(unitRange.toString(),dateRange.toString());
        portalStatistics.setAccessNums(accessNums);
        return new ObjectRestResponse<>().data(portalStatistics).msg("查询成功！");
    }

    /**
     *
     * @param orgCode
     * @return
     */
    public List<AccessNum> accessNums(String orgCode,String date) throws Exception{
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orgCode);
        criteria.andEqualTo("deleted","0");
        Org org = orgBiz.selectByExample(example).get(0);
        Integer orgLevel = org.getOrgLevel();
        List<Org> orgList = orgBiz.findOrgByLevelAndParentId(orgCode,orgLevel+1);
        if(StringUtils.isEmpty(date)){
            List<AccessNum> accessNums = gateLogBiz.findLogCountByOrgCode(orgList,DateUntil.getMonth(),DateUntil.getEndMonth());
            return accessNums;
        }
        if(StringUtils.equals(CommonConstants.JIN_RI,date)){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            List<AccessNum> accessNums = gateLogBiz.
                    findLogCountByOrgCode(orgList,fmt.format(DateUntil.getTimeDay(-1)),fmt.format(DateUntil.getToday()));
            return accessNums;
        }
        if(StringUtils.equals(CommonConstants.BEN_ZHOU,date)){
            List<AccessNum> accessNums = gateLogBiz.
                    findLogCountByOrgCode(orgList,DateUntil.getWeek(),DateUntil.getEndWeek());
            return accessNums;
        }
        if(StringUtils.equals(CommonConstants.BEN_YUE,date)){
            List<AccessNum> accessNums = gateLogBiz.
                    findLogCountByOrgCode(orgList,DateUntil.getMonth(),DateUntil.getEndMonth());
            return accessNums;
        }
        List<AccessNum> accessNums = gateLogBiz.findLogCountByOrgCode(orgList,null,null);
        return accessNums;
    }
    /**
     * 获取总访问量 所有请求之和
     * @return
     */
    public int getTotalAccess(){
        Example example = new Example(GateLog.class);
        return gateLogBiz.selectCountByExample(example);
    }

    /**
     * 获取日访问量  前一天的请求综合
     * @return
     * @throws Exception
     */
    public int getDayAccess(int num) throws Exception{
        Example example = new Example(GateLog.class);
        Date dateYes = DateUntil.getTimeDay(num);
        Date dateTod = DateUntil.getToday();
        Example.Criteria criteria = example.createCriteria();
        criteria.andBetween("crtTime",dateYes,dateTod);
        Integer dayAccess = gateLogBiz.selectCountByExample(example);
        return dayAccess;
    }
    /**
     * 获取去年总访问量  去年所有请求之和
     * @return
     * @throws Exception
     */
    public int getLastYearTotalAccess() throws Exception{
        Example example = new Example(GateLog.class);
        Date LastYear = DateUntil.getBeforeYear();
        Date year = DateUntil.getYear();
        Example.Criteria criteria = example.createCriteria();
        criteria.andBetween("crtTime",LastYear,year);
        Integer dayAccess = gateLogBiz.selectCountByExample(example);
        return dayAccess;
    }

}
