package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.ILarkSearchLogFeign;
import com.github.hollykunge.security.search.dto.GateLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:11 2020/3/17
 */
@Slf4j
@Component
public class ILarkSearchLogHystrix implements ILarkSearchLogFeign {
    @Override
    public GateLogDto getGateLogDocuments() {
        log.error("分页获取日志列表失败，已被降级...");
        return null;
    }

    @Override
    public List<GateLogDto> getGatelogs() {
        log.error("获取所有日志列表失败，已被降级...");
        return null;
    }
}
