package com.github.hollykunge.security.admin.rpc;

import com.github.hollykunge.security.admin.api.log.LogInfo;
import com.github.hollykunge.security.admin.api.service.AdminLogServiceFeignClient;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.entity.GateLog;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-07-01 14:39
 */
@RequestMapping("api")
@RestController
public class LogRest implements AdminLogServiceFeignClient {
    @Autowired
    private GateLogBiz gateLogBiz;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private OrgBiz orgBiz;
    @Override
    @RequestMapping(value="/log/save",method = RequestMethod.POST)
    public @ResponseBody void saveLog(@RequestBody LogInfo info){
        GateLog log = new GateLog();
        BeanUtils.copyProperties(info,log);
        User user = userBiz.getUserByUserPid(info.getPid());
        Org orgTemp = new Org();
        orgTemp.setOrgCode(user.getOrgCode());
        Org org = orgBiz.selectOne(orgTemp);
        setOrg(log,org);
        gateLogBiz.insertSelective(log);
    }
    private void setOrg(GateLog log,Org org){
        log.setOrgCode(org.getOrgCode());
        log.setOrgName(org.getOrgName());
        log.setPathCode(org.getPathCode());
        log.setPathName(org.getPathName());
    }
}
