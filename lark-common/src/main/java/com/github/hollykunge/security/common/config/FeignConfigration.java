package com.github.hollykunge.security.common.config;

import com.github.hollykunge.security.common.aop.FeignInvokeHandler;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.Assert;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhhongyu
 * @description: feign定制组件
 * @since: Create in 9:18 2020/5/13
 */
@Configuration
public class FeignConfigration {
    @Value("${feign.aop}")
    private String pointcut;

    @Bean
    @ConditionalOnProperty(prefix = "feign.hystrix",name = "enabled",havingValue = "true")
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        if(Assert.strNull(pointcut)){
            throw new BaseException("系统必须配置feign所在的位置，请配置 feign.aop 属性");
        }
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(pointcut);
        advisor.setAdvice(new FeignInvokeHandler());
        return advisor;
    }
}
