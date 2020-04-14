package com.github.hollykunge.security.common.util;

import com.github.hollykunge.security.common.constant.RequestHeaderConstants;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhhongyu
 * @description: 内网中request请求头参数获取（dnname，clientip）工具类
 * @since: Create in 14:28 2020/4/13
 */
public class IntranetRequestHeaderUtils {
    public static String getDnName(HttpServletRequest request){
        String dnname = request.getHeader(RequestHeaderConstants.DN_NAME);
        if(StringUtils.isEmpty(dnname)){
            return null;
        }
        return request.getHeader(dnname);
    }

    public static String getClientIp(HttpServletRequest request){
        String clientIp = request.getHeader(RequestHeaderConstants.CLIENT_IP);
        if(StringUtils.isEmpty(clientIp)){
            return null;
        }
        return request.getHeader(clientIp);
    }
}
