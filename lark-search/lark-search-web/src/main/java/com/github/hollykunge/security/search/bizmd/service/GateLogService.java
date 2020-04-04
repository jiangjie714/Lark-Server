package com.github.hollykunge.security.search.bizmd.service;

import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.search.dto.GateLogDto;

import java.util.List;

/**
 * @author: zhhongyu
 * @description: 网关日志
 * @since: Create in 15:56 2020/3/16
 */
public interface GateLogService {
    /**
     * 分页获取es中网关日志列表
     * @param query
     * @return
     * @throws Exception
     */
    List<GateLogDto> page(Query query) throws Exception;

    List<GateLogDto> all()throws Exception;
}
