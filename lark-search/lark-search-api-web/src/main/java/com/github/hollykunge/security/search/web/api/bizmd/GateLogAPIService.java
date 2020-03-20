package com.github.hollykunge.security.search.web.api.bizmd;


import com.github.hollykunge.security.search.dto.GateLogDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 搜索服务，网关日志对外提供接口
 * @since: Create in 15:19 2020/2/27
 */
public interface GateLogAPIService {
    @PostMapping("/gatelog/page")
    List<GateLogDto> page(@RequestBody Map params) throws Exception;
    @GetMapping("/gatelog/all")
    List<GateLogDto> all()throws Exception;
}
