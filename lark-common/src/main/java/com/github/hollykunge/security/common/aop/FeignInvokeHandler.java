package com.github.hollykunge.security.common.aop;


import com.github.hollykunge.security.common.dictionary.HttpReponseStatusEnum;
import com.github.hollykunge.security.common.exception.server.FeignExecutException;
import com.github.hollykunge.security.common.msg.FeignResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author: zhhongyu
 * @description: springAop invoke feign
 * @since: Create in 9:13 2020/5/13
 */
public class FeignInvokeHandler implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object feignResult = invocation.proceed();
        if(feignResult instanceof FeignResponse){
            FeignResponse response = (FeignResponse) feignResult;
            int status = response.getStatus();
            if(status == HttpReponseStatusEnum.SYSTEM_ERROR.value()){
                throw new FeignExecutException(response.getMessage());
            }
        }
        return feignResult;
    }
}
