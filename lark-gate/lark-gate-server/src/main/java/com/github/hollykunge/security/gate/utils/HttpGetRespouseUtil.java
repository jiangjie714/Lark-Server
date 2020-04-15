package com.github.hollykunge.security.gate.utils;

import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: zhhongyu
 * @description: 解析httpget请求的响应体
 * @since: Create in 13:33 2020/4/14
 */
public class HttpGetRespouseUtil {
    public static String setResponse(RequestContext ctx) {
        InputStream stream = ctx.getResponseDataStream();
        try {
            String body = IOUtils.toString(stream);
            RequestContext.getCurrentContext().setResponseBody(body);
            return body;
        } catch (IOException e) {
            ctx.setResponseDataStream(stream);
        }
        return null;
    }
}
