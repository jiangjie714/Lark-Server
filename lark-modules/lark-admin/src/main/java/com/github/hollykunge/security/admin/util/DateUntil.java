package com.github.hollykunge.security.admin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUntil {
    /**
     * 获取当前时间的指定天数之前的时间
     * @return date
     */
    public static Date getTimeDay(int num) throws  Exception{
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH,num);
        String dateStr = fmt.format(calendar.getTime());
        Date date = fmt.parse(dateStr);
        return date;
    }

    /**
     * 获取今天时间
     * @return
     */
    public static Date getToday() throws Exception{
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.parse(fmt.format(date));
    }

    /**
     * 获取当前时间的本周第一天
     * @return
     */
    public static String getWeek(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        return fmt.format(time);
    }

    /**
     * 获取本周最后一天
     * @return
     */
    public static String getEndWeek(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time=cal.getTime();
        return fmt.format(time);
    }
    /**
     * 获取本月 月初时间
     * @return
     * @throws Exception
     */
    public static String getMonth() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    /**
     * 获取指定月 月末时间
     * @return
     * @throws Exception
     */
    public static String getEndMonth() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return format.format(cale.getTime());
    }
    /**
     * 获取指定月 月初时间
     * @return
     * @throws Exception
     */
    public static String getBeforeMonth(int num) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.add(Calendar.MONTH, num);
        return format.format(cale.getTime());
    }
    /**
     * 获取当前时间的前一年 年初时间
     * @return date
     */
    public static Date getBeforeYear() throws Exception{
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.YEAR,-1);
        String dateStr = fmt.format(calendar.getTime());
        System.out.println(dateStr+ "-01-01");
        return fmt.parse(dateStr);
    }

    /**
     * 获取今年 年初时间
     * @return
     * @throws Exception
     */
    public static Date getYear() throws Exception{
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(new Date());
        Date startTime = format.parse(time + "-01-01");
        return startTime;
    }
}
