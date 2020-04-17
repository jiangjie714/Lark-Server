package com.github.hollykunge.security.admin.config;

import com.github.hollykunge.security.auth.client.config.SysAuthConfig;
import com.github.hollykunge.security.auth.client.config.UserAuthConfig;
import com.github.hollykunge.security.auth.client.jwt.UserAuthUtil;
import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author dd
 */
@Slf4j
@WebFilter(filterName = "sysUserRequestFilter", urlPatterns = "/api/org/treeComponent")
public class SysUserRequestFilter implements Filter {
    @Autowired
    private SysAuthConfig sysAuthConfig;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private UserAuthConfig userAuthConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("sysUserRequestFilter init....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        try {
            IJWTInfo infoFromToken = userAuthUtil.getInfoFromToken(req.getHeader("token"));
            if(infoFromToken != null &&
                    !Objects.equals(infoFromToken.getId(),sysAuthConfig.getSysUsername())){
                filterChain.doFilter(request, response);
                return;
            }
            Map<String, Object> parameter = new HashMap(16);
            parameter.put("userOrgCode", sysAuthConfig.getSysOrgCode());
            ParameterRequestWrapper wrapper = new ParameterRequestWrapper(req, parameter);
            filterChain.doFilter(wrapper, response);
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        log.info("sysUserRequestFilter destroy...");
    }
}