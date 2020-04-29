package com.workhub.z.servicechat.aop;

import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.config.ZzChatLog;
import com.workhub.z.servicechat.service.ZzLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.util.Arrays;

//切片 记录文件上传下载日志采集
//注：aop是线程不安全的，且无法配置成多例
@Aspect
@Component
public class OperateLogAspect {
    private static Logger log = LoggerFactory.getLogger(OperateLogAspect.class);
    @Autowired
    ZzLogService zzLogService;
    //切入点 类似注入一个operateLogAspect 同时定义了哪些类的方法被切入
    @Pointcut("execution(public * com.workhub.z.servicechat.service.impl..*.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzLogServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzPrivateMsgServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzMessageInfoServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzGroupStatusServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzMegReadLogServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzMsgReadRelationServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzGroupApproveLogServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzRequireApproveAuthorityServiceImpl.*(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzDictionaryWordsServiceImpl.getSecretWordList40(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzDictionaryWordsServiceImpl.getSecretWordList60(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzUserGroupServiceImpl.queryInGroup(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzGroupServiceImpl.queryGroupUserIdListByGroupId(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzGroupServiceImpl.queryById(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzGroupServiceImpl.getGroupUserList(..)) " +
            "&& !execution(public * com.workhub.z.servicechat.service.impl.ZzAtServiceImpl.*(..)) "
    )

    public void operateLogAspect(){}

    //环绕通知
    @Around(value = "operateLogAspect()")
    public Object logAround (ProceedingJoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        boolean isSocket = false;//是否socket日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes==null){//socket无法获取request
            isSocket = true;
        }
        HttpServletRequest request = null;

        if(!isSocket){//socket无法获取request
            request = attributes.getRequest();
        }
        ZzChatLog zzChatLog = new ZzChatLog();
        zzChatLog.setId(RandomId.getUUID());
        zzChatLog.setSuccessFlg("1");

        if(isSocket){
            zzChatLog.setUserName("");
        }else{
            String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
            zzChatLog.setUserName(userName);
        }

        if(isSocket){
            zzChatLog.setUserId("");
        }else{
            String userId = request.getHeader("userId");
            zzChatLog.setUserId(userId==null?"":userId);
        }

        // 记录下请求内容
        if(isSocket){
            zzChatLog.setUrl("");
        }else{
            String url = request.getRequestURL().toString();
            if(url.length()>500){
                url = url.substring(0,499);
            }
            zzChatLog.setUrl(url);
        }


        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        if(method.length()>500){
            method = method.substring(0,499);
        }
        zzChatLog.setMethod(method);

        if(isSocket){
            zzChatLog.setUserIp("");
        }else{
            zzChatLog.setUserIp(getIRealIPAddr(request));
        }



        Object[] oArr = joinPoint.getArgs();
        if (oArr != null && oArr.length > 0){
            for (int i = 0; i < oArr.length; i++) {
                if (oArr[i] instanceof MultipartFile || oArr[i] instanceof File){
                    oArr[i] = "上传文件不打印";
                }
            }
            String args = Arrays.toString(oArr);
            if(args.length()>200){
                args = args.substring(0,199);
            }
            zzChatLog.setParameters(args);
        }

        Object obj = null;
        try{
            obj = joinPoint.proceed();
        }catch (Exception e){
            zzChatLog.setSuccessFlg("0");
            log.error(Common.getExceptionMessage(e));
            zzChatLog.setErrorInf(Common.getExceptionMessage(e).substring(0,499));
        }
        //原方法执行结束，打印这行日志
        if (obj != null && obj instanceof byte[]){
            //log.info("{} 方法的结果是文件流,不输出",name);
            zzChatLog.setReturnResult("文件流，不具体打印");
        } else if (obj != null){
            //String resultJson = JsonUtils.getInstance().toJson(result);
            String resultJson = obj.toString();
            if (resultJson != null && resultJson.length() > 500){
                resultJson = resultJson.substring(0,499) + "...结果过长...";
            }
            zzChatLog.setReturnResult(resultJson);
            //log.info("{} 方法的结果是 {}",name,resultJson);
        }
        try{
            zzLogService.log(zzChatLog);
        }catch (Exception e){
            log.error(Common.getExceptionMessage(e));
        }

        //返回方法返回参数
        return obj;
    }

    /**
     * 获取客户端真实IP
     * */
    private String getIRealIPAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))    {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)   || "null".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)    || "null".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}