package com.github.hollykunge.security.common.util;

import com.github.hollykunge.security.common.exception.BaseException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验是否包含特殊字符工具类
 * @author zhhongyu
 */
public class SpecialStrUtils {
    public static boolean check(String inputStr){
        if(inputStr == null){
            throw new BaseException("校验的字符串不能为空...");
        }
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p=Pattern.compile(regEx);
        Matcher m=p.matcher(inputStr);
        return m.find();
    }
}
