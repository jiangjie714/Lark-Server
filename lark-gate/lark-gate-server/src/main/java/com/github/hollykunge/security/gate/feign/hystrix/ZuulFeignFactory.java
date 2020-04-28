package com.github.hollykunge.security.gate.feign.hystrix;

import com.github.hollykunge.security.gate.feign.ILarkSearchFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description: 网关服务降级工厂
 * @since: Create in 15:30 2020/4/27
 */
@Component
public class ZuulFeignFactory implements FallbackFactory<ILarkSearchFeign> {
    private final LarkSearchHystrix larkSearchHystrix;

    public ZuulFeignFactory(LarkSearchHystrix larkSearchHystrix) {
        this.larkSearchHystrix = larkSearchHystrix;
    }

    @Override
    public ILarkSearchFeign create(Throwable throwable) {
        larkSearchHystrix.setThrowable(throwable);
        return larkSearchHystrix;
    }
}
