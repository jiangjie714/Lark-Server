package com.github.hollykunge.security.admin.rpc.ignore;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.admin.api.authority.*;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.entity.GateLog;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.rpc.ignore.service.IgnoreService;
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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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
        portalStatistics.setTotalAccess(new Long(totalAccess));
        //获取日访问量  昨天的请求综合
        int dayAccess = getAccess(CommonConstants.YESTERDAY);
        portalStatistics.setDayAccess(new Long(dayAccess));
        //获取今年总访问量 今年所有请求之和
        int yearTotalAccess = getAccess(CommonConstants.THIS_YEAR);
        //获取去年总访问量  去年所有请求之和
        int  lastYearTotalAccess = getAccess(CommonConstants.LAST_YEAR);
        //如果增长率为负 修改为0
        if(yearTotalAccess<lastYearTotalAccess){
            portalStatistics.setTotalRate(0.00);
        }else{
            //如果去年增长量为0  被除数修改为1
            if(lastYearTotalAccess==0){
                lastYearTotalAccess=1;
            }
            //总增长率（今年的请求之和-去年请求之和）/去年请求之和
            double accessDiff = new Double(totalAccess-lastYearTotalAccess).doubleValue();
            double  lastYearTotalAccessDouble = new Double(lastYearTotalAccess).doubleValue();
            String result = new DecimalFormat("0.00").format(accessDiff/lastYearTotalAccessDouble);
            portalStatistics.setTotalRate(Math.ceil(Double.parseDouble(result)));
            //log.info("年增长率"+result);
        }
        //获取日访问量  前天的请求综合
        int dayAccessTwo = getAccess(CommonConstants.BEFOR_YESTERDAY);
        if(dayAccess<dayAccessTwo){
            portalStatistics.setDayRate(0.00);
        }else{
            if(dayAccessTwo==0){
                dayAccessTwo=1;
            }
            //日增长率（昨天请求之和-前天请求之和）/前天请求之和
            double dayAccessDiff = new Double(dayAccess-dayAccessTwo).doubleValue();
            double  dayAccessTwoDouble = new Double(dayAccessTwo).doubleValue();
            String resultDay = new DecimalFormat("0.00").format(dayAccessDiff/dayAccessTwoDouble);
            portalStatistics.setDayRate(Math.ceil(Double.parseDouble(resultDay)));
        }
        portalStatistics.setAccessNums(accessNums("0010",CommonConstants.BEN_YUE));
        portalStatistics.setMessageNums(messageNums("0010",CommonConstants.BEN_YUE));
        portalStatistics.setFileNums(fileNums("0010",CommonConstants.BEN_YUE));
        portalStatistics.setGroupNums(groupNums("0010",CommonConstants.BEN_YUE));
        //饼图
        portalStatistics.setSourceOrg(getSourceOrg("0010",CommonConstants.BEN_YUE));
          portalStatistics.setNodes(getNodes());
          portalStatistics.setLinks(getLinks());
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
        //柱状图
        List<AccessNum> accessNums = accessNums(unitRange.toString(),dateRange.toString());
        portalStatistics.setAccessNums(accessNums);
        portalStatistics.setMessageNums(messageNums(unitRange.toString(),dateRange.toString()));
        portalStatistics.setFileNums(fileNums(unitRange.toString(),dateRange.toString()));
        portalStatistics.setGroupNums(groupNums(unitRange.toString(),dateRange.toString()));
        //饼图
        portalStatistics.setSourceOrg(getSourceOrg(unitRange.toString(),dateRange.toString()));
        return new ObjectRestResponse<>().data(portalStatistics).msg("查询成功！");
    }

    /**
     *获取柱状图数据
     * @param orgCode
     * @return
     */
    public List<AccessNum> accessNums(String orgCode,String date) throws Exception{
        List<Org>orgList = getOrg(orgCode);
        if(StringUtils.equals(CommonConstants.JIN_RI,date)){
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
     * 获取饼图数据
     * @return
     */
    public List<SourceOrg> getSourceOrg(String orgCode,String date) throws Exception{
        List<Org>orgList = getOrg(orgCode);
        if(StringUtils.equals(CommonConstants.JIN_RI,date)){
            //SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            List<SourceOrg> sourceOrgs = gateLogBiz.
                    findLogCountByOrgCodeAll(orgCode,orgList,CommonConstants.JIN_RI_TYPE);
            return sourceOrgs;
        }
        if(StringUtils.equals(CommonConstants.BEN_ZHOU,date)){
            List<SourceOrg> sourceOrgs = gateLogBiz.
                    findLogCountByOrgCodeAll(orgCode,orgList,CommonConstants.BEN_ZHOU_TYPE);
            return sourceOrgs;
        }
        if(StringUtils.equals(CommonConstants.BEN_YUE,date)){
            List<SourceOrg> sourceOrgs = gateLogBiz.
                    findLogCountByOrgCodeAll(orgCode,orgList,CommonConstants.BEN_YUE_TYPE);
            return sourceOrgs;
        }
        if(StringUtils.equals(CommonConstants.QUAN_BU,date)) {
            List<SourceOrg> sourceOrgs = gateLogBiz.findLogCountByOrgCodeAll(orgCode,orgList, CommonConstants.QUAN_BU_TYPE);
            return sourceOrgs;
        }
        return null;
    }

    /**
     * 获取散点关系图数据 点的大小
     * @return
     */
    public List<Node> getNodes() throws Exception{
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id","99999");
        criteria.andNotEqualTo("id","0010");
        criteria.andIsNotNull("orgCode");
        criteria.andEqualTo("deleted","0");
        List<Org> org = orgBiz.selectByExample(example);
        List<Node> nodes = gateLogBiz.findNodeLink(org);
        return nodes;
    }

    /**
     * 获取散点关系图数据 link线
     * @return
     * @throws Exception
     */
    public List<Link> getLinks() throws Exception{
        List<Link> links = new ArrayList<>();
        ObjectRestResponse groupUser = ignoreService.groupUserStatistics(null);
        Object groupUserResult = groupUser.getResult();
        String msgJson = JSONArray.toJSONString(groupUserResult);
        List<StatisticsGroupOrgVo> statisticsGroupUserVos= JSONArray.parseArray(msgJson, StatisticsGroupOrgVo.class);
        for (int i = 0; i <statisticsGroupUserVos.size(); i++) {
            List<StatisticsGroupOrgDetailVo> detailVos = statisticsGroupUserVos.get(i).getOrgList();
            Link linkS = new Link();
            Link linkT = new Link();
            Link linkP = new Link();
            Link link = new Link();
            StatisticsGroupOrgDetailVo last = detailVos.get(detailVos.size()-1);
            StatisticsGroupOrgDetailVo first = detailVos.get(0);
            linkS.setSource(first.getOrgCode());
            linkS.setTarget(last.getOrgCode());
            links.add(linkS);
            linkT.setSource(last.getOrgCode());
            linkT.setTarget(first.getOrgCode());
            links.add(linkT);
            linkP.setSource(first.getParentId());
            linkP.setTarget(last.getParentId());
            links.add(linkP);
            link.setSource(last.getParentId());
            link.setTarget(first.getParentId());
            links.add(link);
            for (int j = 0; j <detailVos.size()-1; j++) {
                Link linkSource = new Link();
                Link linkTarget = new Link();
                Link linkParent = new Link();
                Link linkParentT = new Link();
                linkSource.setSource(detailVos.get(j).getOrgCode());
                linkSource.setTarget(detailVos.get(j+1).getOrgCode());
                linkTarget.setSource(detailVos.get(j+1).getOrgCode());
                linkTarget.setTarget(detailVos.get(j).getOrgCode());
                links.add(linkSource);
                links.add(linkTarget);
                linkParent.setSource(detailVos.get(j).getParentId());
                linkParent.setTarget(detailVos.get(j+1).getParentId());
                links.add(linkParent);
                linkParentT.setSource(detailVos.get(j+1).getParentId());
                linkParentT.setTarget(detailVos.get(j).getParentId());
                links.add(linkParentT);
            }
        }
        List<Link> linkList = setLink();
        links = links.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getSource()+";"+o.getTarget()))),
                        ArrayList::new));
        links.addAll(linkList);
        return links;
    }

    /**
     * 部门连接线
     * @return
     */
    public List<Link> setLink(){
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgLevel","3");
        criteria.andIsNotNull("orgCode");
        criteria.andEqualTo("deleted","0");
        List<Org> orgList = orgBiz.selectByExample(example);
        Example exampl = new Example(Org.class);
        Example.Criteria criteri = exampl.createCriteria();
        criteri.andNotEqualTo("orgLevel",1);
        criteri.andNotEqualTo("orgLevel",2);
        criteri.andNotEqualTo("orgLevel",3);
        criteria.andIsNotNull("orgCode");
        criteria.andEqualTo("deleted","0");
        List<Org> orgs = orgBiz.selectByExample(exampl);
        for (Org org:orgList){
//            for (Org o :orgs){
//                Link link = new Link();
//                link.setSource(org.getOrgCode());
//                link.setTarget(o.getOrgCode());
//                links.add(link);
//            }
            treeMenuList(orgs,org.getId());
        }
        return linkList;
    }
    List<Link> linkList = new ArrayList<>();
    public List<Link> treeMenuList(List<Org> orgList, String  pid){
            for(Org mu: orgList){
                //遍历出父id等于参数的id，add进子节点集合
                if(StringUtils.equals(pid,mu.getParentId())){
                    //递归遍历下一级
                    Link link = new Link();
                    link.setSource(pid);
                    link.setTarget(mu.getOrgCode());
                    linkList.add(link);
                    treeMenuList(orgList,mu.getId());
                }
            }
            return linkList;
        }




//        List<Link> links = new ArrayList<>();
//        if(orgList==null||orgList.size()==0){
//            return links;
//        }
//        for (Org org:orgList){
//            Example exampl = new Example(Org.class);
//            Example.Criteria criteri = exampl.createCriteria();
//            criteri.andEqualTo("parentId",org.getId());
//            criteri.andEqualTo("deleted","0");
//            List<Org> orgs = orgBiz.selectByExample(exampl);
//            for (Org o :orgs){
//                Link link = new Link();
//                link.setSource(org.getOrgCode());
//                link.setTarget(o.getOrgCode());
//                links.add(link);
//            }
//            return f(orgs);
//        }
//        return links;
    //}
    /**
     * 获取部门
     * @param orgCode
     * @return
     */
    public List<Org> getOrg(String orgCode){
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orgCode);
        criteria.andEqualTo("deleted","0");
        Org org = orgBiz.selectByExample(example).get(0);
        Integer orgLevel = org.getOrgLevel();
        List<Org> orgList = orgBiz.findOrgByLevelAndParentId(orgCode,orgLevel+1);
        return orgList;
    }
    /**
     * 获取柱状图消息量排行
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
     * 获取柱状图文件量排行
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
     * 获取柱状图群组量排行
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
