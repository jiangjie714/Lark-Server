/*   
 * @(#)LogServiceAspect.java       2018/11/21  
 *   
 * 百得利集团拥有完全的版权   
 * 使用者必须经过许可   
 */
package com.workhub.z.servicechat.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Service日志统一打印切面
 *
 * @author huangboyang
 * @since JDK 1.8
 */
@Component
@Aspect
public class LogServiceAspect {

    private static Logger logger = LoggerFactory.getLogger(LogServiceAspect.class);

    /**
     * 	切点
     *
     * @author huangboyang
     * @since JDK 1.8
     */
    @Pointcut("execution (* com.workhub.z.servicechat.service.impl..*.*(..))")
    public void serviceLogAop() {
    }

    /**
     * 	切点处理函数
     *
     * @author huangboyang
     * @since JDK 1.8
     */
    @Around("serviceLogAop()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        logger.info("Service日志打印 ===== {}.{}() start =====,参数:{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), argsToString(point.getArgs()));
        DateTime startTime = new DateTime();
        DateTime endTime = null;
        Interval interval = null;
        Object response = null;
        try {
            //执行该方法
            response = point.proceed();
        } catch (Exception e) {
           logger.error("", e);

            endTime = new DateTime();
            interval = new Interval(startTime, endTime);
            logger.info("Service日志打印 ===== {}.{}() end =====,响应时间:{}毫秒,响应内容:{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), interval.toDurationMillis());
            throw e;
        }
        endTime = new DateTime();
        interval = new Interval(startTime, endTime);
        logger.info("Service日志打印 ===== {}.{}() end =====,响应时间:{}毫秒,响应内容:{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), interval.toDurationMillis(), argsToString(response));
        return response;
    }

    /**
     * 	参数转换函数
     *
     * @author huangboyang
     * @param object	参数对象
     * @return 文字串
     * @since JDK 1.8
     */
    private String argsToString(Object object) {
        if(object == null){
            return null;
        }
        try {
            //SerializerFeature.IgnoreNonFieldGetter 防止内存溢出 对于大数据量如file 解析空处理
            return JSON.toJSONString(object, SerializerFeature.IgnoreNonFieldGetter);
        }
        catch (Exception e) {
            if(!( e instanceof IllegalStateException)) {
                logger.error("", e);
            }else {
                if(object instanceof Object[]) {
                    Object[] oArr = (Object[])object;
                    for(int i = 0; i < oArr.length; i++) {
                        if(oArr[i] instanceof RequestFacade) {
                            oArr[i] = "HttpServletRequest";
                        }else if(oArr[i] instanceof ResponseFacade) {
                            oArr[i] = "HttpServletResponse";
                        }
                    }
                    try {
                        return JSON.toJSONString(object);
                    }catch (Exception ex) {
                        logger.error("", ex);
                    }
                }
            }
        }
        return String.valueOf(object);
    }
}
