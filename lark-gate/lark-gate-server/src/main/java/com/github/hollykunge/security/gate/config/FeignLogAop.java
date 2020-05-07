package com.github.hollykunge.security.gate.config;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.search.web.api.response.SearchObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Aspect
@Slf4j
public class FeignLogAop {

    /**
     * 功能描述: 定义切点切人feign.client包下所有类
     * @Return: void
     * @Author: zh
     */
    @Pointcut("execution(* com.github.hollykunge.security.gate.feign.*.*(..)) ")
    public void executeService(){}
    /**
     * 功能描述: 方法执行返回值时
     * @Param: [obj]
     * @Return: void
     * @Author: zh
     */
    @AfterReturning(value = "executeService()" , returning="obj")
    public void afterAdviceReturn(Object obj){
        log.info("feign后置aop已经被执行...");
        if(obj instanceof SearchObjectRestResponse){
            SearchObjectRestResponse response = (SearchObjectRestResponse) obj;
            int status = response.getStatus();
            if(status == 500){
                throw new BaseException("FEIGN 接口 调用异常...");
            }
        }
    }
}
