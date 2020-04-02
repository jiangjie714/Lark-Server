package com.github.hollykunge.larkconfig;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author fansq
 * 过滤请求进行body内容的修改，重新包装，因为github自动刷新的时候添加了json多余内容
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/actuator/bus-refresh",filterName = "UrlConfigFilter")
public class UrlConfigFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        filterChain. doFilter(new ConfigRequestWrapper(request),servletResponse);

    }

    @Override
    public void destroy() {

    }
}
