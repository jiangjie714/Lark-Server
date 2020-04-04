package com.github.hollykunge.security.search.bizmd.web;

import com.alibaba.excel.util.StringUtils;
import com.github.hollykunge.security.search.bizmd.service.GateLoginStatisticsService;
import com.github.hollykunge.security.search.web.api.bizmd.GateLoginStatisticsApiService;
import com.github.hollykunge.security.search.web.api.exception.SearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 网关日志，登录统计
 * @since: Create in 10:25 2020/3/18
 */
@RestController
public class GateLoginStatisticsRestApi implements GateLoginStatisticsApiService {
    @Autowired
    private GateLoginStatisticsService gateLoginStatisticsService;

    @Override
    public @ResponseBody
    Map<String, Long> loginNumStatistics(@RequestBody List<String> orgPathCode) throws Exception {
        if (StringUtils.isEmpty(orgPathCode)) {
            throw new SearchException("部门的全路径不能为空...");
        }
        return gateLoginStatisticsService.loginNumStatistics(orgPathCode);
    }

    @Override
    public @ResponseBody
    Map<String, Long> loginTimesStatistics(@RequestBody List<String> orgPathCode) throws Exception {
        if (StringUtils.isEmpty(orgPathCode)) {
            throw new SearchException("部门的全路径不能为空...");
        }
        return gateLoginStatisticsService.loginTimesStatistics(orgPathCode);
    }
}
