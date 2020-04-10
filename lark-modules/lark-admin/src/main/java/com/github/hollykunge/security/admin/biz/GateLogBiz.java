package com.github.hollykunge.security.admin.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.admin.api.authority.*;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.entity.*;
import com.github.hollykunge.security.admin.mapper.GateLogMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.admin.redisKey.StatisticsKeyGenerator;
import com.github.hollykunge.security.admin.rpc.ignore.service.IgnoreService;
import com.github.hollykunge.security.admin.rpc.service.UserRestService;
import com.github.hollykunge.security.admin.util.ListUtil;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-07-01 14:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GateLogBiz extends BaseBiz<GateLogMapper, GateLog> {
    @Autowired
    private RoleBiz roleBiz;
    @Autowired
    private UserRestService userRestService;
    @Autowired
    private RoleUserMapMapper roleUserMapMapper;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private GateLogMapper gateLogMapper;
    @Value("${role.code.system}")
    private String sysRoleCode;
    @Value("${role.code.log}")
    private String logRoleCode;
    @Value("${role.code.security}")
    private String securityRoleCode;

    @Autowired
    private OrgBiz orgBiz;

    @Autowired
    private IgnoreService ignoreService;
    public List<GateLog> gateLogExport() {
        return mapper.gateLogExport();
    }

    @Override
    public void insert(GateLog entity) {
        mapper.insert(entity);
    }

    @Override
    public void insertSelective(GateLog entity) {
        entity.setId(UUIDUtils.generateShortUuid());
        mapper.insertSelective(entity);
    }

    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    public TableResultResponse<GateLog> selectByQuery(Query query) {
        Example example = new Example(GateLog.class);
        if (query.entrySet().size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                boolean exitTime = this.setCreTimeCondition(criteria, entry);
                if (exitTime) {
                    continue;
                }
                if (entry.getValue().toString() != null || !"".equals(entry.getValue().toString())) {
                    criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                }
            }
        }
        example.setOrderByClause("CRT_TIME DESC");
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<GateLog> list = mapper.selectByExample(example);
        return new TableResultResponse<GateLog>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), list);
    }

    @Override
    public TableResultResponse<GateLog> selectByQueryM(Query query, String type) {
        Example example = new Example(GateLog.class);
        if (query.entrySet().size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                List<String> valueList = new ArrayList<>();
                if (entry.getValue() instanceof List) {
                    valueList = (List<String>) entry.getValue();
                    if (type.equals("log")) {
                        criteria.andNotEqualTo(entry.getKey(), valueList.get(0)).andNotEqualTo(entry.getKey(), valueList.get(1));
                    }
                    if (type.equals("Security")) {
                        criteria.andIn(entry.getKey(), valueList);
                    }
                    continue;
                }
                boolean exitTime = this.setCreTimeCondition(criteria, entry);
                if (exitTime) {
                    continue;
                }
                if (entry.getValue().toString() != null || !"".equals(entry.getValue().toString())) {
                    criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                }
            }
        }
        example.setOrderByClause("CRT_TIME DESC");
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<GateLog> list = mapper.selectByExample(example);
        return new TableResultResponse<GateLog>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), list);
    }

    /**
     * 含有三员的分页限制
     *
     * @param query
     * @param pid
     * @return
     */
    public TableResultResponse<GateLog> pageByRole(Query query, String pid) {
        Example example = new Example(GateLog.class);
        Example.Criteria criteria = null;
        //先判断该用户的角色是否为三员
        if (StringUtils.isEmpty(pid)) {
            throw new BaseException("pid不能为空或null...");
        }
        //查询pid所对应的角色
        String userInfo = userRestService.getUserInfo(pid, null);
        List<AdminUser> adminUsers = JSONArray.parseArray(userInfo,AdminUser.class);
        if (adminUsers == null || adminUsers.size() == 0) {
            throw new BaseException("没有查询到该用户的角色...");
        }
        Role role = roleBiz.selectById(adminUsers.get(0).getRoleId());
        List<String> pids = getSanyuan(role.getCode(),adminUsers.get(0).getOrgCode());
        //证明是三员中的一位
        if (pids != null) {
            criteria = example.createCriteria();
            //防止数据过高，将pids拆分成500个
            List<List<String>> lists = ListUtil.splitList(pids, 500);
            if (Objects.equals(role.getCode(), logRoleCode)) {
                for(List<String> pidList : lists){
                    criteria.andNotIn("pid",pidList);
                }
            }
            if (Objects.equals(role.getCode(), securityRoleCode)) {
                for(List<String> pidList : lists){
                    criteria.andIn("pid",pidList);
                }
            }
        }
        if (query.entrySet().size() > 0) {
            if(criteria == null){
                criteria = example.createCriteria();
            }
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if (entry.getValue().toString() != null || !"".equals(entry.getValue().toString())) {
                    criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                }
            }
        }
        example.setOrderByClause("CRT_TIME DESC");
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<GateLog> list = mapper.selectByExample(example);
        return new TableResultResponse<GateLog>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), list);
    }

    private List<String> getSanyuan(String roleCode,String orgCode) {
        //日志管理员,安全管理员
        if (Objects.equals(roleCode, logRoleCode) ||
                Objects.equals(roleCode, securityRoleCode) ||
                Objects.equals(roleCode, sysRoleCode)) {
            List<String> pids = getlogAndsysPids(sysRoleCode, logRoleCode, orgCode);
            return pids;
        }
        return null;
    }

    /**
     * 获取登录用户角色所属组织下的所有的系统管理员和安全管理员的pids
     *
     * @return
     */
    private List<String> getlogAndsysPids(String sysRoleCode, String logRoleCode,String orgCode) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isEmpty(sysRoleCode) || StringUtils.isEmpty(logRoleCode)) {
            return result;
        }
        //
        String[] strings = new String[]{sysRoleCode, logRoleCode};
        Example roleExample = new Example(Role.class);
        Example.Criteria criteria = roleExample.createCriteria();
        criteria.andIn("code", Arrays.asList(strings));
        List<Role> roles = roleBiz.selectByExample(roleExample);
        //可能产生配置错了的情况
        if(roles ==null || roles.size() == 0){
            return null;
        }
        Example roleUserExample = new Example(RoleUserMap.class);
        Example.Criteria roleUsercriteria = null;
        if(roles != null && roles.size() != 0){
            roleUsercriteria = roleUserExample.createCriteria();
            for(Role role : roles){
                roleUsercriteria.orEqualTo("roleId",role.getId());
            }
        }
        List<RoleUserMap> roleUserMaps = roleUserMapMapper.selectByExample(roleUserExample);
        //证明没有配置所对应的人员，返回空
        if(roleUserMaps == null || roleUserMaps.size() == 0){
            return null;
        }
        //此处不做人员是否删除过滤，日志查看是可以看到删除人员操作的日志
        roleUserMaps.forEach(roleUserMap -> result.add(roleUserMap.getUserId()));
        Example userExcample = new Example(User.class);
        Example.Criteria userCriteria = userExcample.createCriteria();
        if(!StringUtils.isEmpty(orgCode)){
            userCriteria.andLike("orgCode",orgCode+"%");
        }
        roleUserMaps.forEach(roleUserMap -> {
            userCriteria.orEqualTo("id",roleUserMap.getUserId());
        });
        List<User> users = userBiz.selectByExample(userExcample);
        //不可能出现，一个userid对应一个pid
        if(users == null || users.size() == 0){
            return null;
        }
        users.forEach(user -> result.add(user.getPId()));
        return result;
    }

    private Date stringToDate(String source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (Exception e) {
        }
        return date;
    }

    private boolean setCreTimeCondition(Example.Criteria criteria, Map.Entry<String, Object> entry) {
        if ("crtTime".equals(entry.getKey())) {
            if (StringUtils.isEmpty(entry.getValue())) {
                throw new BaseException("输入时间不能为空...");
            }
            String[] dateSplits = entry.getValue().toString().trim().split(",");
            if (dateSplits.length != 0) {
                Date beginDate = this.stringToDate(dateSplits[0], "yyyy-MM-dd");
                Date endDate = this.stringToDate(dateSplits[1], "yyyy-MM-dd");
                criteria.andBetween(entry.getKey(), beginDate, endDate);
            }
            return true;
        }
        return false;
    }

    /**
     * 统计功能 获取登录量计数
     *
     * @return
     */
    public List<AccessNum> findLogCountByOrgCode(List<Org> orgList,String type){
        List<AccessNum> accessNums = new ArrayList<>();
        for(Org o:orgList){
            AccessNum accessNum = new AccessNum();
            accessNum.setX(o.getOrgName());
            Long num = gateLogMapper.getOrgCodeLogNum(o.getId(),type,"/api/admin/user/front/info");
            accessNum.setY(num);
            accessNums.add(accessNum);
        }
        accessNums = accessNums.stream().sorted(Comparator.comparing(AccessNum::getY).
                reversed()).collect(Collectors.toList());
        return accessNums;
    }

    public Long findLogCountByOrgCodeAll(String orgCode,String type){
            Long num = gateLogMapper.getOrgCodeLogNum(orgCode,type,"");
            return num;
    }
    public List<SourceOrg> findLogCountByOrgCodeAll(String orgCode,List<Org> orgList,String type){
        //总活动量
        Long numAll = findLogCountByOrgCodeAll(orgCode,type);
        List<SourceOrg> sourceOrgs = new ArrayList<>();
        for(Org o:orgList){
            SourceOrg sourceOrg = new SourceOrg();
            sourceOrg.setItem(o.getOrgName());
            Long num = gateLogMapper.getOrgCodeLogNum(o.getId(),type,"");
            if(numAll==0||num==0){
                sourceOrg.setCount(0.0);
            }else{
                double numAllD = new Double(numAll).doubleValue();
                double  numD = new Double(num).doubleValue();
                String n = new DecimalFormat("0.0").format(numD/numAllD);
                sourceOrg.setCount(Double.parseDouble(n));
            }
            sourceOrgs.add(sourceOrg);
        }
        sourceOrgs = sourceOrgs.stream().sorted(Comparator.comparing(SourceOrg::getCount).
                reversed()).collect(Collectors.toList());
        return sourceOrgs;
    }

    /**
     * 获取散点图点的大小
     * @param orgList
     * @return
     */
    public List<Node> findNodeLink(List<Org> orgList){
        List<Node> nodes = new ArrayList<>();
        for(Org o:orgList){
            Node node = new Node();
            Long num = gateLogMapper.getCountLog(o.getId());
            node.setId(o.getOrgCode());
            node.setName(o.getOrgName());
            node.setParnetId(o.getParentId());
            node.setLevel(o.getOrgLevel());
            node.setSymbolSize(getNormalizeDistance(num));
            nodes.add(node);
        }
        return nodes;
    }

    public Double getNormalizeDistance(long num){
        double min = 0.0;
        Example example = new Example(GateLog.class);
        int max = gateLogMapper.selectCountByExample(example);
        double normalize = ((num-min)/(max-min))*100;
        return normalize;
    }

    /**
     * 获取总访问量 所有请求之和
     * @return
     */
    @Cache(key = "ignore:getTotalAccess",expire = 1440)
    public int getTotalAccess(){
        Example example = new Example(GateLog.class);
        return gateLogMapper.selectCountByExample(example);
    }

    /**
     * 获取访问量
     * @return
     * @throws Exception
     */
    @Cache(key = "#type",expire = 1440)
    public int getAccess(String type) throws Exception{
        int access = gateLogMapper.getAccess(type);
        return access;
    }

    /**
     *获取柱状图数据
     * @param orgCode
     * @return
     */
    @Cache(key="accessNums",generator = StatisticsKeyGenerator.class,expire = 1440)
    public List<AccessNum> accessNums(String orgCode,String date) throws Exception{
        List<Org>orgList = getOrg(orgCode);
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.JIN_RI,date)){
            List<AccessNum> accessNums =
                    findLogCountByOrgCode(orgList,CommonConstants.JIN_RI_TYPE);
            return accessNums;
        }
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.BEN_ZHOU,date)){
            List<AccessNum> accessNums =
                    findLogCountByOrgCode(orgList,CommonConstants.BEN_ZHOU_TYPE);
            return accessNums;
        }
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.BEN_YUE,date)){
            List<AccessNum> accessNums =
                    findLogCountByOrgCode(orgList,CommonConstants.BEN_YUE_TYPE);
            return accessNums;
        }
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.QUAN_BU,date)) {
            List<AccessNum> accessNums = findLogCountByOrgCode(orgList, CommonConstants.QUAN_BU_TYPE);
            return accessNums;
        }
        return null;
    }

    /**
     * 获取柱状图消息量排行
     * @param orgCode
     * @param date
     * @return
     * @throws Exception
     */
    @Cache(key="messageNums",generator = StatisticsKeyGenerator.class,expire = 1440)
    public List<MessageNums> messageNums(String orgCode, String date) throws Exception{
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
    @Cache(key="fileNums",generator = StatisticsKeyGenerator.class,expire = 1440)
    public List<FileNum> fileNums(String orgCode, String date) throws Exception{
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
    @Cache(key="groupNums",generator = StatisticsKeyGenerator.class,expire = 1440)
    public List<GroupNum> groupNums(String orgCode,String date) throws Exception{
        ObjectRestResponse groupStatistics = ignoreService.groupStatistics(orgCode,date);
        Object msgGroup = groupStatistics.getResult();
        String msgJsonGroup = JSONArray.toJSONString(msgGroup);
        List<GroupNum> groupNums= JSONArray.parseArray(msgJsonGroup, GroupNum.class);
        return groupNums;
    }

    /**
     * 获取饼图数据
     * @return
     */
    @Cache(key="getSourceOrg",generator = StatisticsKeyGenerator.class,expire = 1440)
    public List<SourceOrg> getSourceOrg(String orgCode,String date) throws Exception{
        List<Org>orgList = getOrg(orgCode);
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.JIN_RI,date)){
            List<SourceOrg> sourceOrgs =
                    findLogCountByOrgCodeAll(orgCode,orgList,CommonConstants.JIN_RI_TYPE);
            return sourceOrgs;
        }
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.BEN_ZHOU,date)){
            List<SourceOrg> sourceOrgs =
                    findLogCountByOrgCodeAll(orgCode,orgList,CommonConstants.BEN_ZHOU_TYPE);
            return sourceOrgs;
        }
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.BEN_YUE,date)){
            List<SourceOrg> sourceOrgs =
                    findLogCountByOrgCodeAll(orgCode,orgList,CommonConstants.BEN_YUE_TYPE);
            return sourceOrgs;
        }
        if(org.apache.commons.lang3.StringUtils.equals(CommonConstants.QUAN_BU,date)) {
            List<SourceOrg> sourceOrgs = findLogCountByOrgCodeAll(orgCode,orgList, CommonConstants.QUAN_BU_TYPE);
            return sourceOrgs;
        }
        return null;
    }

    /**
     * 获取散点关系图数据 点的大小
     * @return
     */
    @Cache(key="getNodes",expire = 1440)
    public List<Node> getNodes() throws Exception{
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id","99999");
        criteria.andNotEqualTo("id","0010");
        criteria.andIsNotNull("orgCode");
        criteria.andEqualTo("deleted","0");
        List<Org> org = orgBiz.selectByExample(example);
        List<Node> nodes = findNodeLink(org);
        return nodes;
    }

    /**
     * 获取散点关系图数据 link线
     * @return
     * @throws Exception
     */
    @Cache(key="getLinks",expire = 1440)
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
        List<Link> linkList = new ArrayList<>();
        for (Org org:orgList){
            treeMenuList(orgs,org.getId(),linkList);
        }
        return linkList;
    }

    public List<Link> treeMenuList(List<Org> orgList, String  pid,List<Link> linkList){
        for(Org mu: orgList){
            //遍历出父id等于参数的id，add进子节点集合
            if(org.apache.commons.lang3.StringUtils.equals(pid,mu.getParentId())){
                //递归遍历下一级
                Link link = new Link();
                link.setSource(pid);
                link.setTarget(mu.getOrgCode());
                linkList.add(link);
                treeMenuList(orgList,mu.getId(),linkList);
            }
        }
        return linkList;
    }

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


}
