package com.github.hollykunge.security.search.bizmd.service;

import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 10:33 2020/3/18
 */
public interface GateLoginStatisticsService {
    /**
     * 登录人数统计
     *
     * @param orgPathCode 组织ids
     *
     * @return
     * @throws Exception
     */
    Map<String, Long> loginNumStatistics(List<String> orgPathCode) throws Exception;

    /**
     * 登录人次统计
     *
     * @param orgPathCode 组织ids
     * @return
     * @throws Exception
     */
    Map<String, Long> loginTimesStatistics(List<String> orgPathCode) throws Exception;
}
