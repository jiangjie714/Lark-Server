package com.github.hollykunge.security.admin.rpc;

import com.github.hollykunge.security.admin.api.log.LogInfo;
import com.github.hollykunge.security.admin.api.service.AdminLogServiceFeignClient;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.entity.GateLog;
import org.springframework.beans.BeanUtils;
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
    @Override
    @RequestMapping(value="/log/save",method = RequestMethod.POST)
    public @ResponseBody void saveLog(@RequestBody LogInfo info){
        GateLog log = new GateLog();
        BeanUtils.copyProperties(info,log);
        gateLogBiz.insertSelective(log);
    }
}
