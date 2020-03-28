package com.github.hollykunge.security.admin.rpc.ignore;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.admin.api.authority.*;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.entity.GateLog;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.rpc.ignore.service.IgnoreService;
import com.github.hollykunge.security.admin.util.DateUtil;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author fansq  ignore
 * @since 20-3-23
 * @deprecation 不走网关并提供给其他服务的接口  ignoreController
 */
@RestController
@RequestMapping("/ignore")
@Slf4j
public class IgnoreController {

    @Autowired
    private GateLogBiz gateLogBiz;
    @Autowired
    private OrgBiz orgBiz;
    @Autowired
    private IgnoreService ignoreService;
    /**
     * 用于门户统计页面数据初始化
     * @param request
     * @return
     */
    @RequestMapping(value = "/statistics/all",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<PortalStatistics> statisticsAll(HttpServletRequest request) throws Exception{
        PortalStatistics portalStatistics = new PortalStatistics();
        //获取总访问量 所有请求之和
        int totalAccess = getTotalAccess();
        //log.info("总访问量"+totalAccess);
        portalStatistics.setTotalAccess(new Long(totalAccess));
        //获取日访问量  昨天的请求综合
        int dayAccess = getAccess(CommonConstants.YESTERDAY);
        //log.info("日访问量"+dayAccess);
        portalStatistics.setDayAccess(new Long(dayAccess));
        //获取今年总访问量 今年所有请求之和
        int yearTotalAccess = getAccess(CommonConstants.THIS_YEAR);
        //log.info("今年访问量"+yearTotalAccess);
        //获取去年总访问量  去年所有请求之和
        int  lastYearTotalAccess = getAccess(CommonConstants.LAST_YEAR);
        //log.info("去年访问量"+lastYearTotalAccess);
        //如果增长率为负 修改为0
        if(yearTotalAccess<lastYearTotalAccess){
            portalStatistics.setTotalRate(0.00);
            log.info("年增长率"+0.00);
        }else{
            //如果去年增长量为0  被除数修改为1
            if(lastYearTotalAccess==0){
                lastYearTotalAccess=1;
            }
            //总增长率（今年的请求之和-去年请求之和）/去年请求之和
            double accessDiff = new Double(totalAccess-lastYearTotalAccess).doubleValue();
            double  lastYearTotalAccessDouble = new Double(lastYearTotalAccess).doubleValue();
            String result = new DecimalFormat("0.00").format(accessDiff/lastYearTotalAccess);
            portalStatistics.setTotalRate(Math.ceil(Double.parseDouble(result)));
            log.info("年增长率"+result);
        }
        //获取日访问量  前天的请求综合
        int dayAccessTwo = getAccess(CommonConstants.BEFOR_YESTERDAY);
        log.info("前天访问量"+dayAccessTwo);
        if(dayAccess<dayAccessTwo){
            portalStatistics.setDayRate(0.00);
            log.info("日增长率"+0.00);
        }else{
            if(dayAccessTwo==0){
                dayAccessTwo=1;
            }
            //日增长率（昨天请求之和-前天请求之和）/前天请求之和
            double dayAccessDiff = new Double(dayAccess-dayAccessTwo).doubleValue();
            double  dayAccessTwoDouble = new Double(dayAccessTwo).doubleValue();
            String resultDay = new DecimalFormat("0.00").format(dayAccessDiff/dayAccessTwoDouble);
            portalStatistics.setDayRate(Math.ceil(Double.parseDouble(resultDay)));
           log.info("日增长率"+resultDay);
        }
        portalStatistics.setAccessNums(accessNums("0010",CommonConstants.BEN_YUE));
        portalStatistics.setMessageNums(messageNums("0010",CommonConstants.BEN_YUE));
        portalStatistics.setFileNums(fileNums("0010",CommonConstants.BEN_YUE));
        portalStatistics.setGroupNums(groupNums("0010",CommonConstants.BEN_YUE));
        return new ObjectRestResponse<>().data(portalStatistics).msg("查询成功！");
    }

    /**
     * 门户统计页面 具体数据获取 例如本单位 本部门 按时间区分 等
     * @param map
     * @return
     */
    @RequestMapping(value = "/statistics/bar",method = RequestMethod.GET)
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
        portalStatistics.setMessageNums(messageNums(unitRange.toString(),dateRange.toString()));
        portalStatistics.setFileNums(fileNums(unitRange.toString(),dateRange.toString()));
        portalStatistics.setGroupNums(groupNums(unitRange.toString(),dateRange.toString()));
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
        if(StringUtils.equals(CommonConstants.JIN_RI,date)){
            //SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            List<AccessNum> accessNums = gateLogBiz.
                    findLogCountByOrgCode(orgList,CommonConstants.JIN_RI_TYPE);
            return accessNums;
        }
        if(StringUtils.equals(CommonConstants.BEN_ZHOU,date)){
            List<AccessNum> accessNums = gateLogBiz.
                    findLogCountByOrgCode(orgList,CommonConstants.BEN_ZHOU_TYPE);
            return accessNums;
        }
        if(StringUtils.equals(CommonConstants.BEN_YUE,date)){
            List<AccessNum> accessNums = gateLogBiz.
                    findLogCountByOrgCode(orgList,CommonConstants.BEN_YUE_TYPE);
            return accessNums;
        }
        if(StringUtils.equals(CommonConstants.QUAN_BU,date)) {
            List<AccessNum> accessNums = gateLogBiz.findLogCountByOrgCode(orgList, CommonConstants.QUAN_BU_TYPE);
            return accessNums;
        }
        return null;
    }

    /**
     * 获取消息量排行
     * @param orgCode
     * @param date
     * @return
     * @throws Exception
     */
    public List<MessageNums> messageNums(String orgCode,String date) throws Exception{
        ObjectRestResponse msgStatistics = ignoreService.msgStatistics(orgCode,date);
        Object msg = msgStatistics.getResult();
        String msgJson = JSONArray.toJSONString(msg);
        List<MessageNums> messageNums= JSONArray.parseArray(msgJson, MessageNums.class);
        return messageNums;
    }

    /**
     * 获取文件量排行
     * @param orgCode
     * @param date
     * @return
     * @throws Exception
     */
    public List<FileNum> fileNums(String orgCode,String date) throws Exception{
        ObjectRestResponse fileStatistics = ignoreService.fileStatistics(orgCode,date);
        Object msgFile = fileStatistics.getResult();
        String msgJsonFile = JSONArray.toJSONString(msgFile);
        List<FileNum> fileNums= JSONArray.parseArray(msgJsonFile, FileNum.class);
        return fileNums;
    }

    /**
     * 获取文群组量排行
     * @param orgCode
     * @param date
     * @return
     * @throws Exception
     */
    public List<GroupNum> groupNums(String orgCode,String date) throws Exception{
        ObjectRestResponse groupStatistics = ignoreService.groupStatistics(orgCode,date);
        Object msgGroup = groupStatistics.getResult();
        String msgJsonGroup = JSONArray.toJSONString(msgGroup);
        List<GroupNum> groupNums= JSONArray.parseArray(msgJsonGroup, GroupNum.class);
        return groupNums;
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
     * 获取访问量
     * @return
     * @throws Exception
     */
    public int getAccess(String type) throws Exception{
        int access = gateLogBiz.getAccess(type);
        return access;
    }
}
