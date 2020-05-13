package com.github.hollykunge.security.common.feign;


import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: zhhongyu
 * @description: 公共的feign工厂类
 * @since: Create in 16:21 2020/4/28
 */
public class BaseFeignFactory<Base extends BaseHystix> extends BaseHystix implements FallbackFactory<Base> {

    @Autowired
    private Base baseHystrix;

    @Override
    public Base create(Throwable throwable) {
        baseHystrix.setThrowable(throwable);
        return baseHystrix;
    }
}
