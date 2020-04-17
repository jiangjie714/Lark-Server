package com.github.hollykunge.security.admin.util;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author: zhhongyu
 * @description: 密码加密工具类
 * <p>自定义加密串，base64转码后进行线程安全md5加密<p/>
 * @since: Create in 8:56 2020/4/17
 */
public class PassWordUtils {
    /**
     * 自定义密匙
     */
    public static final String MD5KYE = "abcdefgABCDEFG12345678";
    public static String encodeBase64(String codeStr){
        Base64 base64 = new Base64();
        try {
            byte[] textByte = codeStr.getBytes("UTF-8");
            return base64.encodeToString(textByte);
        }   catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用线程安全的DigestUtils，工具类进行md5串计算
     * @param text
     * @return
     */
    public static String md5(String text){
        text = encodeBase64(text);
        return DigestUtils.md5Hex(text + MD5KYE);
    }

    public static void main(String[] args) {
        String s = PassWordUtils.md5("123456");
        System.out.println(s);
    }
}
