package com.github.hollykunge.security.gate.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author: zhhongyu
 * @description: 网关执行错误过滤器
 * @since: Create in 13:25 2020/4/20
 */
@Component
@Slf4j
public class ZuulExceptionFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().containsKey("throwable");
    }

    @Override
    public Object run(){
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            Object e = ctx.get("throwable");
            if (e != null && e instanceof ZuulException) {
                ZuulException zuulException = (ZuulException) e;
                // 删除该异常信息,不然在下一个过滤器中还会被执行处理
                ctx.remove("throwable");
                // 响应给客户端信息
                HttpServletResponse response = ctx.getResponse();
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pw = response.getWriter();
                pw.write(JSONObject.toJSONString(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),zuulException.getCause().getMessage())));
                pw.close();
            }
        } catch (Exception ex) {
            log.error("Exception filtering in custom error filter", ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }
}
