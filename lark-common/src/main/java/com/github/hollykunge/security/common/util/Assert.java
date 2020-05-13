package com.github.hollykunge.security.common.util;

import org.springframework.util.StringUtils;

/**
 * @author: zhhongyu
 * @description: 系统断言工具类
 * @since: Create in 9:21 2020/5/13
 */
public class Assert {
    public static boolean strNull(String args){
        if(StringUtils.isEmpty(args)){
            return true;
        }
        return false;
    }
}
