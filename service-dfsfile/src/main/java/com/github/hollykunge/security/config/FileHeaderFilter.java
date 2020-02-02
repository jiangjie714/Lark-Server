package com.github.hollykunge.security.config;

import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.common.util.ClientUtil;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.jwt.FileJwtInfo;
import com.github.hollykunge.security.vo.JwtInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: zhhongyu
 * @description: 文件服务定义请求头过滤器
 * @since: Create in 15:37 2019/12/2
 */
@Slf4j
@WebFilter(filterName = "fileHeaderFilter", urlPatterns = "/fdfs/file/*")
public class FileHeaderFilter implements Filter {
    @Autowired
    private FileJwtInfo fileJwtInfo;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("FileHeaderFilter init....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
        try {
            IJWTInfo infoFromToken = fileJwtInfo.getJwtInfo(req);
            if(infoFromToken != null){
                requestWrapper.addHeader("userId",infoFromToken.getUniqueName());
                requestWrapper.addHeader("userName",infoFromToken.getName());
                String host = StringUtils.isEmpty(req.getHeader("clientIp")) ?
                        req.getHeader("clientIp") : ClientUtil.getClientIp(req);
                requestWrapper.addHeader("userHost",host);
                requestWrapper.addHeader("userSecretLevel",infoFromToken.getSecretLevel());
                chain.doFilter(requestWrapper, response);
                return;
            }
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
        // Goes to default servlet.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("sysUserRequestFilter destroy...");
    }
}
