package com.github.hollykunge.security.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author fansq
 * @since 20-3-26
 * @deprecation 时间操作类
 */
@Slf4j
public class DateUtil {
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
        //log.info("前"+num+"的时间为："+dateStr);
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
        String d = fmt.format(date);
        //log.info("今天的时间为："+d);
        return fmt.parse(d);
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
        String d = fmt.format(time);
        //log.info("本周第一天时间为："+d);
        return d;
    }

    /**
     * 获取本周最后一天
     * @return
     */
    public static String getEndWeek(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.add(Calendar.WEEK_OF_MONTH, 1);
        Date time=cal.getTime();
        String d = fmt.format(time);
        //log.info("下一周第一天时间为："+d);
        return d;
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
        String d = format.format(cale.getTime());
        //log.info("本月月初时间为："+d);
        return d;
    }

    /**
     * 获取 下个月月初时间
     * @return
     * @throws Exception
     */
    public static String getEndMonth() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.add(Calendar.MONTH, 1);
        String d = format.format(cale.getTime());
        //log.info("下一个月月初时间为："+d);
        return d;
    }

    /**
     * 获取当前时间的前一年 年初时间
     * @return date
     */
    public static Date getBeforeYear() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
        calendar.add(Calendar.YEAR,-1);
        String dateStr = fmt.format(calendar.getTime());
        //log.info("前一年年初时间为："+dateStr+ "-01-01");
        return fmt.parse(dateStr+ "-01-01");
    }
    /**
     * 获取当前时间的下一年 年初时间
     * @return date
     */
    public static Date getLasterYear() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
        calendar.add(Calendar.YEAR,1);
        String dateStr = fmt.format(calendar.getTime());
        //log.info("下一年的年初时间为："+dateStr+ "-01-01");
        return fmt.parse(dateStr+ "-01-01");
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
        SimpleDateFormat f = new SimpleDateFormat("yyyy");
        String time = f.format(new Date());
        Date startTime = format.parse(time + "-01-01");
        //log.info("今年的年初时间为："+time+ "-01-01");
        return startTime;
    }
}
