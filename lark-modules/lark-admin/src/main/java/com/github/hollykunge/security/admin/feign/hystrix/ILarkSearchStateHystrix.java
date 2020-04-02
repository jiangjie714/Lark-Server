package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.ILarkSearchStateFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 17:03 2020/3/19
 */
@Component
@Slf4j
public class ILarkSearchStateHystrix implements ILarkSearchStateFeign {
    @Override
    public Map<String, Long> loginNumStatistics(List<String> orgPathCode) throws Exception {
        log.error("ILarkSearchStateFeign调用search服务接口loginNumStatistics失败，接口已经降级");
        return new HashMap<>();
    }
    @Override
    public Map<String, Long> loginTimesStatistics(List<String> orgPathCode) throws Exception {
        log.error("ILarkSearchStateFeign调用search服务接口loginTimesStatistics失败，接口已经降级");
        return new HashMap<>();
    }
}
