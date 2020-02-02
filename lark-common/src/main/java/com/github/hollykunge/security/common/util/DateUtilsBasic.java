package com.github.hollykunge.security.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 所有关于日期的基本操作.
 *
 * @author: holly
 * @since: 2019/6/22
 */
public class DateUtilsBasic {

    protected DateUtilsBasic() {
        throw new UnsupportedOperationException();
    }

    /** The Constant YYYY_MM_DD_HH_MM_SS. */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /** The Constant FULL. */
    public static final int FULL = DateFormat.FULL;

    /**
     * 获取当前时间.
     * @return String
     * 				以yyyy-MM-dd HH:mm:ss格式标准
     */
    public static String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return sdf.format(new Date());

    }

    /**
     * 获取当前日期
     * @param format
     *            	日期/时间格式
     * @return String
     * 				已格式化的时间字符串。
     */
    public static String getCurDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());

    }

    /**
     * 将给定日期格式化为字符串.
     * @param date
     *            要格式化为时间字符串的时间值。
     * @param format
     *            日期/时间格式
     * @return String
     * 			  已格式化的时间字符串。
     */
    public static String getDateStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);

    }

    /**
     * 获取现在的年份.
     * @return int
     * 			 公元年
     */
    public static int getCurYear() {
        return new GregorianCalendar().get(Calendar.YEAR);
    }

    /**
     * 获取现在的月份.
     * @return int
     * 			  0表示年的第一个月
     */
    public static int getCurMonth() {
        return new GregorianCalendar().get(Calendar.MONTH);
    }

    /**
     * 获取当前日期所在月份的天数，假如今天为 2011 年 5 月 3 日,则返回值为 3
     * @return int
     * 			  当前日期在当前月份中的天数，一个月中第一天的值为 1。
     */
    public static int getCurDay() {
        return new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
    }
}
