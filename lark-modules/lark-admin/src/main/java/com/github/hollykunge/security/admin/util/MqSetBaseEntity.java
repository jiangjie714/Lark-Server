package com.github.hollykunge.security.admin.util;

import com.github.hollykunge.security.common.util.UUIDUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 使用反射，mq新增数据赋值baseEntity基础数据
 * @author zhhongyu
 * @since 2019-07-23
 */
public class MqSetBaseEntity {
    public static <T> void setCreatData(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UnknownHostException, InstantiationException {
        Class<?> aClass = t.getClass();
        Method setCrtTime = aClass.getMethod("setCrtTime",Date.class);
        Method setCrtUser = aClass.getMethod("setCrtUser",String.class);
        Method setCrtName = aClass.getMethod("setCrtName",String.class);
        Method setCrtHost = aClass.getMethod("setCrtHost",String.class);
//        Method setId = aClass.getMethod("setId", String.class);
        setCrtTime.invoke(t,new Date());
        setCrtUser.invoke(t,"mq");
        setCrtName.invoke(t,"mq");
//        setId.invoke(t, UUIDUtils.generateShortUuid());
        setCrtHost.invoke(t,InetAddress.getLocalHost().toString());
    }

    public static <T> T setUpdate(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UnknownHostException {
        Method setCrtTime = t.getClass().getMethod("setUpdTime",Date.class);
        Method setUpdUser = t.getClass().getMethod("setUpdUser",String.class);
        Method setUpdName = t.getClass().getMethod("setUpdName",String.class);
        Method setUpdHost = t.getClass().getMethod("setUpdHost",String.class);
        setCrtTime.invoke(t,new Date());
        setUpdUser.invoke(t,"mq");
        setUpdName.invoke(t,"mq");
        setUpdHost.invoke(t,InetAddress.getLocalHost().toString());
        return t;
    }
}
