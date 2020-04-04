package com.github.hollykunge.security.search.web.api.bizmd;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 对外提供apiservice接口
 * @since: Create in 10:26 2020/3/18
 */
public interface GateLoginStatisticsApiService {
    /**
     * 部门下的登录人数统计
     *
     * @param orgPathCode 部门的全路径
     * @return 统计信息
     * @throws Exception 异常
     */
    @PostMapping("/org/loginNum")
    @ResponseBody
    Map<String, Long> loginNumStatistics(@RequestBody List<String> orgPathCode) throws Exception;

    /**
     * 部门下的登录人次统计
     *
     * @param orgPathCode 部门的全路径
     * @return 统计信息
     * @throws Exception 异常
     */
    @PostMapping("/org/loginTimes")
    @ResponseBody
    Map<String, Long> loginTimesStatistics(@RequestBody List<String> orgPathCode) throws Exception;
}
