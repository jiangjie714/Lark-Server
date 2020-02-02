package com.github.hollykunge.security.common.aop;

import com.github.hollykunge.security.common.annotation.FilterSecretLevelHandler;
import com.github.hollykunge.security.common.config.AutoConfiguration;
import com.github.hollykunge.security.common.dictionary.SecretFileterConditionEnum;
import com.github.hollykunge.security.common.parser.IDynamicSecretGenerator;
import com.github.hollykunge.security.common.parser.IUserSecretGenerator;
import com.github.hollykunge.security.common.parser.impl.DefaultDynamicKey;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.common.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: zhhongyu
 * @description: 人员密级过滤切面
 * @since: Create in 15:06 2019/12/3
 */
@Slf4j
@Aspect
public class FilterSecretLevelAspect {
    private static final String FIELD = "secretLevel";

    @Autowired
    private IDynamicSecretGenerator dynamicSecretGenerator;
    private ConcurrentHashMap<String, IDynamicSecretGenerator> generatorMap = new ConcurrentHashMap<String, IDynamicSecretGenerator>();

    @Pointcut("@annotation(com.github.hollykunge.security.common.annotation.FilterSecretLevelHandler)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(anno)")
    public <T> Object interceptor(ProceedingJoinPoint point, FilterSecretLevelHandler anno)
            throws Throwable {
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Object result = point.proceed();
        if(result instanceof List){
            List<T> joinData = (List<T>) result;
            String[] secrets = getSecrets(anno, parameterTypes, args);
            //没有密级，不过滤
            if(secrets.length == 0){
                return result;
            }
            //判断是否过滤方式
            //1.为in（在其密级范围）的过滤
            if(SecretFileterConditionEnum.in.equals(anno.condition())){
                return getSecretFilter(joinData,secrets,anno.condition());
            }
            //2.为out（不在其密级范围）的过滤
            if(SecretFileterConditionEnum.not.equals(anno.condition())){
                return getSecretFilter(joinData,secrets,anno.condition());
            }
        }
        return result;
    }

    private String[] getSecrets(FilterSecretLevelHandler anno,
                                Class<?>[] parameterTypes,
                                Object[] arguments) {
        String generatorClsName = anno.dynamicSecret().getName();
        IDynamicSecretGenerator iDynamicSecretGenerator = null;
        try {
            if (anno.dynamicSecret().equals(DefaultDynamicKey.class)) {
                iDynamicSecretGenerator = dynamicSecretGenerator;
            } else {
                if (generatorMap.contains(generatorClsName)) {
                    iDynamicSecretGenerator = generatorMap.get(generatorClsName);
                } else {
                    iDynamicSecretGenerator = anno.dynamicSecret().newInstance();
                    generatorMap.put(generatorClsName, iDynamicSecretGenerator);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
        String key = "";
        if(!StringUtils.isEmpty(anno.secret())){
            key = anno.secret();
        }
        //如果使用人员，都配置了secret和secrets默认使用secrets
        if(!StringUtils.isEmpty(anno.secrets())){
            key = anno.secrets();
        }
        return iDynamicSecretGenerator.getSecret(key,parameterTypes,arguments);
    }

    /**
     * 过滤在密级范围内
     * @param lists
     * @param <T>
     * @return
     */
    private <T> List<T> getSecretFilter(List<T> lists,String[] secrets,SecretFileterConditionEnum conditionEnum){
        IUserSecretGenerator userSecretGenerator = dynamicSecretGenerator.getUserSecretGenerator();
        //获取当前登录人密级
        String userSecret = userSecretGenerator.getUserSecret();
        if(lists == null || lists.size() == 0){
            return lists;
        }
        //返回数据体中没有secretLevel的属性则不进行过滤
        if(!ReflectionUtils.hasField(lists.get(0),FIELD)){
            return lists;
        }
        lists = lists.parallelStream().filter(ob -> {
            Object secretLevel = ReflectionUtils.invokeGetter(ob, FIELD);
            //密级为null的过滤掉，不显示
            if(secretLevel == null){
                return false;
            }
            //高于当前登录人的密级不显示
            if(!getUserSecret(userSecret,secretLevel)){
                return false;
            }
            //没有配置或提供比对的密级范围，则默认不进行过滤
            if(secrets == null || secrets.length == 0){
                return true;
            }
            if(conditionEnum.equals(SecretFileterConditionEnum.in)){
                return Arrays.asList(secrets).contains(secretLevel);
            }
            if(conditionEnum.equals(SecretFileterConditionEnum.not)){
                return !Arrays.asList(secrets).contains(secretLevel);
            }
            return true;
        }).collect(Collectors.toList());
        return lists;
    }

    /**
     * 和当前登录人的密级进行判断
     * @param userSecret
     * @param secretLevel
     * @return
     */
    private boolean getUserSecret(String userSecret,Object secretLevel){
        //当前登录没有密级的，不参与过滤
        if(StringUtils.isEmpty(userSecret)){
            return true;
        }
        int user = Integer.parseInt(userSecret);
        int secret = 0;
        if(secretLevel instanceof String){
            secret = Integer.parseInt((String)secretLevel);
        }
        if(secretLevel instanceof Integer){
            secret = (Integer) secretLevel;
        }
        if(secret > user){
            return false;
        }
        return true;
    }
}
