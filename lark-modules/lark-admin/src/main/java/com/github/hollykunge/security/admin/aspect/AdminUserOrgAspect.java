package com.github.hollykunge.security.admin.aspect;

import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.dictionary.UserOrgStatusEnum;
import com.github.hollykunge.security.common.util.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhhongyu
 * @description: 人员切面
 * @since: Create in 10:44 2019/12/2
 */
@Aspect
@Service
public class AdminUserOrgAspect {
    private static final String DELETED = "deleted";
    private static final String ORDERID = "orderId";

    @Pointcut("@annotation(com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler)")
    public void aspect() {
    }

    /**
     * 后置拦截器，过滤所有的人员数据
     *
     * @param point
     * @param anno
     * @return
     * @throws Throwable
     */
    @Around("aspect()&&@annotation(anno)")
    public Object interceptor(ProceedingJoinPoint point, FilterByDeletedAndOrderHandler anno)
            throws Throwable {
        Object result = point.proceed();
        return setRule(result);
    }

    /**
     * 设置过滤规则
     *
     * @param args
     * @param <T>
     * @return
     */
    private <T> Object setRule(Object args) {
        if(args instanceof List){
            List<T> result = (List<T>) args;
            List<T> finalLists = setFilter(result);
            finalLists = setSort(finalLists);
            return finalLists;
        }
        return args;
    }

    private <T> List<T> setFilter(List<T> lists){
        if(lists.size() == 0 || !ReflectionUtils.hasField(lists.get(0),DELETED)){
            return lists;
        }
        List<T> finalLists = lists.stream().filter((T user) -> {
            if (!ReflectionUtils.hasField(user, DELETED)) {
                return true;
            }
            Object o = ReflectionUtils.invokeGetter(user, DELETED);
            if (o == null) {
                return false;
            }
            if (o instanceof String) {
                String deleted = (String) o;
                if (UserOrgStatusEnum.DELETED.getValue().equals(deleted)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        return finalLists;
    }

    private <T> List<T> setSort(List<T> lists){
        if(lists.size() == 0 || !ReflectionUtils.hasField(lists.get(0),ORDERID)){
            return lists;
        }
        lists.sort((t1, t2) -> {
            Object getterUser1 = ReflectionUtils.invokeGetter(t1, ORDERID);
            Object getterUser2 = ReflectionUtils.invokeGetter(t2, ORDERID);
            if (getterUser1 instanceof Long && getterUser2 instanceof Long) {
                Long longUser1 = (Long) getterUser1;
                Long longUser2 = (Long) getterUser2;
                return longUser1.compareTo(longUser2);
            }
            //不是long类型的字段，不进行排序
            return 0;
        });
        return lists;
    }
}
