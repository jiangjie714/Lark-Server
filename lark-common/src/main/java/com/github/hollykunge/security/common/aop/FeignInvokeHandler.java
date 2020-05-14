package com.github.hollykunge.security.common.aop;


import com.github.hollykunge.security.common.feign.FeignResponseEvent;
import com.github.hollykunge.security.common.msg.FeignResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author: zhhongyu
 * @description: springAop invoke feign
 * @since: Create in 9:13 2020/5/13
 */
public class FeignInvokeHandler implements MethodInterceptor {
    private final ApplicationContext applicationContext;
    public FeignInvokeHandler(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object feignResult = invocation.proceed();
        if(feignResult instanceof FeignResponse){
            FeignResponse response = (FeignResponse) feignResult;
            FeignResponseEvent event = new FeignResponseEvent(this,response);
            applicationContext.publishEvent(event);
        }
        return feignResult;
    }
}
