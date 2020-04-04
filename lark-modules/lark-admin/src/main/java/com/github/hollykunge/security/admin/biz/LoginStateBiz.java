package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.feign.ILarkSearchStateFeign;
import com.github.hollykunge.security.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhhongyu
 * @description: 登录统计业务
 * @since: Create in 17:12 2020/3/19
 */
@Service
public class LoginStateBiz {
    @Autowired
    private ILarkSearchStateFeign stateFeign;
    @Autowired
    private OrgBiz orgBiz;
    private List<String> getChildOrgs(String orgCode){
        if(StringUtils.isEmpty(orgCode)){
            throw new BaseException("orgCode不能为空...");
        }
        //查询子部门，只查询下一层子部门
        Org org = orgBiz.selectById(orgCode);
        if(org == null){
            throw new BaseException("不存在的部门...");
        }
        if(StringUtils.isEmpty(org.getPathCode())){
            throw new BaseException("不存在部门pathcode...");
        }
        Org temp = new Org();
        temp.setParentId(orgCode);
        List<Org> orgs = orgBiz.selectList(temp);
        List<String> collect = orgs.stream().map(Org::getPathCode).filter(entity -> !StringUtils.isEmpty(entity)).collect(Collectors.toList());
        return collect;
    }

    /**
     * 统计部门下登录人数
     * @param orgCode
     * @return
     */
    public List<Map<String,Long>> orgLoginNum(String orgCode) throws Exception {
        Map<String, Long> loginNum = stateFeign.loginNumStatistics(getChildOrgs(orgCode));
        return getOrgNameForStates(loginNum);
    }

    /**
     * 统计部门下登录人次
     * @param orgCode
     * @return
     */
    public List<Map<String,Long>> orgLoginTimes(String orgCode) throws Exception {
        Map<String, Long> loginTimes = stateFeign.loginTimesStatistics(getChildOrgs(orgCode));
        return getOrgNameForStates(loginTimes);
    }

    /**
     * 将key的code转为name（此方法只走一遍数据库，防止创建太多的数据库连接，筛选就使用java去进行了）
     * @param states
     * @return
     */
    public List<Map<String,Long>> getOrgNameForStates(Map<String,Long> states){
        List<Map<String,Long>> result = new ArrayList<>();
        List<Org> orgs = orgBiz.selectListAll();
        states.forEach((key,value) -> {
            List<Org> collect = orgs.stream().map(org -> Objects.equals(org.getPathCode(), key) ? org : null)
                    .filter(entity -> !StringUtils.isEmpty(entity))
                    .collect(Collectors.toList());
            if(collect != null && collect.size() > 0){
                Org org = collect.get(0);
                Map<String,Long> state = new HashMap<>();
                //这个判断，去除一下脏数据，太烦了
                if(!StringUtils.isEmpty(org.getOrgName())){
                    state.put(org.getOrgName(), states.get(key));
                    result.add(state);
                }
            }
        });
        return result;
    }
}
