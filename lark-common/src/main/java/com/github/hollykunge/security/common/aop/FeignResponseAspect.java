package com.github.hollykunge.security.common.aop;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.server.FeignExecutException;
import com.github.hollykunge.security.common.msg.FeignResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Aspect
@Slf4j
public class FeignResponseAspect {

    /**
     * 功能描述: 定义切点切人com.github.hollykunge.security.*.feign.下类
     * @Return: void
     * @Author: zh
     */
    @Pointcut("execution(* com.github.hollykunge.security.*.feign.*.*(..)) ")
    public void executeService(){}
    /**
     * 功能描述: 方法执行返回值时
     * @Param: [obj]
     * @Return: void
     * @Author: zh
     */
    @AfterReturning(value = "executeService()" , returning="obj")
    public void afterAdviceReturn(Object obj){
        if(obj instanceof FeignResponse){
            FeignResponse response = (FeignResponse) obj;
            int status = response.getStatus();
            if(status == 500){
                throw new FeignExecutException(response.getMessage());
            }
        }
    }
}
