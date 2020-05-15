package com.github.hollykunge.security.log.utils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 17:05 2020/3/18
 */
public class DateUtils {
    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(Date date) throws ParseException {
        long ts = date.getTime();
        return ts;
    }
}
