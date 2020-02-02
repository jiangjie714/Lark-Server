package com.workhub.larktools.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * author:admin
 * description:
 * date:2019/8/13 13:49
 **/
public class CommonUtil {
    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
    //objec转字符串 null-> ""
    public static String nulToEmptyString(Object object){
        try{
            if(object==null){
                return "";
            }
            return  object.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error(getExceptionMessage(e));
        }
        return  null;
    }
    //异常信息获取
    public static String getExceptionMessage(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
    /**
     *@Description: 把entity null 转换成空字符串,时间转换成当前时间，long转换成0L
     *@Param: string 接收的实体
     *@return: boolean
     *@Author: zhuqz
     *@date: 2019/06/10
     */
    public static<T> void putEntityNullToEmptyString (T enity) throws Exception{
        if(enity==null){
            return;
        }
        //遍历enity类 成员为String类型 属性为空的全部替换为“”
        Field[] fields = enity.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // 获取属性的名字
            String name = fields[i].getName();
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 获取属性的类型
            String type = fields[i].getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.String")) {
                Method m = enity.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    String string = new String("");
                    Object[] objects = new Object[1];
                    objects[0] = string;
                    m.invoke(enity, objects);
                }
            }
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.util.Date")) {
                Method m = enity.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                Date value = (Date) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    Date date = new Date();
                    Object[] objects = new Object[1];
                    objects[0] = date;
                    m.invoke(enity, objects);
                }
            }
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.Double")) {
                Method m = enity.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                Double value = (Double) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    double data = 0.0;
                    Object[] objects = new Object[1];
                    objects[0] = data;
                    m.invoke(enity, objects);
                }
            }
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("long")) {
                boolean hasFun = true;
                Method m = null;
                try{
                    m = enity.getClass().getMethod("get" + name);
                }catch (NoSuchMethodException e){//可能是序列化的long
                    hasFun=false;
                }
                if(!hasFun){
                    continue;
                }
                // 调用getter方法获取属性值
                Long value = (Long) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    long data = 0L;
                    Object[] objects = new Object[1];
                    objects[0] = data;
                    m.invoke(enity, objects);
                }
            }
        }
    }
}
