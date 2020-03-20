package com.github.hollykunge.security.search.bizmd.web;

import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.search.bizmd.service.GateLogService;
import com.github.hollykunge.security.search.dto.GateLogDto;
import com.github.hollykunge.security.search.web.api.bizmd.GateLogAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @author: zhhongyu
 * @description: 网关日志对外提供接口
 * @since: Create in 15:15 2020/2/27
 */
@RestController
public class GateLogRestApi implements GateLogAPIService{
    @Autowired
    private GateLogService gateLogService;
    @Override
    public List<GateLogDto> page(@RequestBody Map params) throws Exception {
        Query pageParams = new Query(params);
        return gateLogService.page(pageParams);
    }
    @Override
    public List<GateLogDto> all()throws Exception{
        return gateLogService.all();
    }

}
