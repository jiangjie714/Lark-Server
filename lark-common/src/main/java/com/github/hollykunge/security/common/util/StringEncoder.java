package com.github.hollykunge.security.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
*@Description: 特殊字符串转义
*@Author: 忠
*@date: 2019/7/8
*/
public class StringEncoder {
    public static String StringEncoder(String temp) throws UnsupportedEncodingException {
       return URLEncoder.encode(temp, "UTF-8").replaceAll("%5B", "\\[")
                .replaceAll("%5D", "\\]").replaceAll("\\+", "%20")
                .replaceAll("%28", "\\(").replaceAll("%29", "\\)")
                .replaceAll("%3B", ";").replaceAll("%40", "@")
                .replaceAll("%23", "\\#").replaceAll("%26", "\\&")
                .replaceAll("%7B", "\\{").replaceAll("%7D", "\\}")
                .replaceAll("%21", "\\!").replaceAll("%24", "\\$")
                .replaceAll("%21", "\\!").replaceAll("%25", "\\%")
                .replaceAll("%5E", "\\^").replaceAll("%2B", "\\+");
    }
}
