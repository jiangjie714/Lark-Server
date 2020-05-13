package com.github.hollykunge.security.common.feign;

/**
 * @author: zhhongyu
 * @description: 公共的hystrix类
 * @since: Create in 16:19 2020/4/28
 */
public class BaseHystix {
    protected Throwable throwable;

    protected void setThrowable(Throwable throwable){
        this.throwable = throwable;
    }
}
