package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.ILarkSearchLogFeign;
import com.github.hollykunge.security.search.dto.GateLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:11 2020/3/17
 */
@Slf4j
@Component
public class ILarkSearchLogHystrix implements ILarkSearchLogFeign {

    @Override
    public List<GateLogDto> page(Map params) throws Exception {
        log.error("分页获取日志列表失败，已被降级...");
        return new ArrayList<>();
    }

    @Override
    public List<GateLogDto> all() throws Exception {
        log.error("获取所有日志列表失败，已被降级...");
        return new ArrayList<>();
    }
}
