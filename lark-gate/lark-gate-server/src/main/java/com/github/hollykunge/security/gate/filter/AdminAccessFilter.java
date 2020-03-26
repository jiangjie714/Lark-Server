package com.github.hollykunge.security.gate.filter;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import com.github.hollykunge.security.auth.client.config.ServiceAuthConfig;
import com.github.hollykunge.security.auth.client.config.SysAuthConfig;
import com.github.hollykunge.security.auth.client.config.UserAuthConfig;
import com.github.hollykunge.security.auth.client.jwt.ServiceAuthUtil;
import com.github.hollykunge.security.auth.client.jwt.UserAuthUtil;
import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.context.BaseContextHandler;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.ClientInvalidException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.auth.TokenErrorResponse;
import com.github.hollykunge.security.common.msg.auth.TokenForbiddenResponse;
import com.github.hollykunge.security.common.util.ClientUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 网关核心权限拦截类
 *
 * @author 协同设计小组
 * @create 2017-06-23 8:25
 */
@Component
@Slf4j
public class AdminAccessFilter extends ZuulFilter {
    @Autowired
    @Lazy
    private AdminUserServiceFeignClient userService;

    @Value("${gate.ignore.startWith}")
    private String startWith;

    @Value("${zuul.prefix}")
    private String zuulPrefix;
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;
    @Autowired
    private SysAuthConfig sysAuthConfig;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI().substring(zuulPrefix.length());

        if(requestUri==null){
            throw new ClientInvalidException("Invalid customer request");
        }
        BaseContextHandler.setToken(null);

        String dnname = request.getHeader(CommonConstants.PERSON_ID_ARG);
        //获取内网网关地址
        String clientIp = request.getHeader(CommonConstants.CLIENT_IP_ARG);
        BaseContextHandler.set(CommonConstants.CLIENT_IP_ARG, clientIp);
        //将院网关ip携带给云雀服务，供其他服务使用
        if (!StringUtils.isEmpty(clientIp)) {
            ctx.addZuulRequestHeader(CommonConstants.CLIENT_IP_ARG, clientIp);
        }
        String body = null;
        if (!ctx.isChunkedRequestBody()&&StringUtils.equals(requestUri,CommonConstants.AUTH_JWT_TOKEN)) {
            try {
                ServletInputStream inp = ctx.getRequest().getInputStream();
                if (inp != null) {
                    body = IOUtils.toString(inp);
                    if(!StringUtils.isEmpty(body)) {
                        JSONObject jsonObject = new JSONObject(body);
                        String username = jsonObject.get("username").toString();
                        if(!StringUtils.isEmpty(username)&&
                                Objects.equals(username,sysAuthConfig.getSysUsername())){
                            ctx.addZuulRequestHeader(CommonConstants.PERSON_ID_ARG,"");
                            return authorization(requestUri,ctx,request);
                        }
                    }
                }
            } catch (Exception e) {
                throw new ClientInvalidException("身份信息编码转化错误...");
            }
        }
        //正常用户名密码登录
        if (StringUtils.isEmpty(dnname)) {
           return authorization(requestUri,ctx,request);
        }
        try {
            dnname = new String(dnname.getBytes(CommonConstants.PERSON_CHAR_SET));
        } catch (UnsupportedEncodingException e) {
            throw new ClientInvalidException("身份信息编码转化错误...");
        }
        String[] userObjects = dnname.trim().split(",", 0);
        String PId = null;
        for (String val :
                userObjects) {
            val = val.trim();
            if (val.indexOf("t=") > -1 || val.indexOf("T=") > -1) {
                PId = val.substring(2, val.length());
            }
        }

        //将dnname设置为身份证信息
        ctx.addZuulRequestHeader(CommonConstants.PERSON_ID_ARG, PId.toLowerCase());
        //秘钥登录
        return authorization(requestUri,ctx,request);
    }

    /**
     * 认证过程
     * @param requestUri
     * @param ctx
     * @param request
     * @return
     */
    private Object authorization(String requestUri,RequestContext ctx,HttpServletRequest request){
        /**
         * 正常用户名密码登录
         */
        if (isStartWith(requestUri)) {
            if(request.getHeader(userAuthConfig.getTokenHeader()) != null){
                ctx.addZuulRequestHeader("token",
                        request.getHeader(userAuthConfig.getTokenHeader()));
            }
            return null;
        }
        IJWTInfo user = null;
        try {
            user = getJWTUser(request, ctx);
        } catch (Exception e) {
            setFailedRequest(JSON.toJSONString(new TokenErrorResponse(e.getMessage())), CommonConstants.HTTP_SUCCESS);
            return null;
        }
        //如果为超级管理员，则干直接通过
        if(Objects.equals(user.getUniqueName(),sysAuthConfig.getSysUsername())){
            this.setCurrentUserInfoAndLog(ctx,user,null);
            return null;
        }
        //根据用户id获取资源列表，包括菜单和菜单功能
        List<FrontPermission> permissionInfos = userService.getPermissionByUserId(user.getId());
        if (permissionInfos.size() > 0) {
            checkUserPermission(requestUri, permissionInfos, ctx, user);
        }
        // 申请客户端密钥头，加到header里传递到下方服务
        ctx.addZuulRequestHeader(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        return null;
    }

    /**
     * 在上下文中设置当前用户信息和操作日志
     */
    private void setCurrentUserInfoAndLog(RequestContext ctx, IJWTInfo user, FrontPermission pm) {
        String host = ClientUtil.getClientIp(ctx.getRequest());
        ctx.addZuulRequestHeader("userId", user.getId());
        try {
            ctx.addZuulRequestHeader("userName", URLEncoder.encode(user.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ctx.addZuulRequestHeader("userHost", ClientUtil.getClientIp(ctx.getRequest()));
        //请求头中增加人员密级
        ctx.addZuulRequestHeader("userSecretLevel", user.getSecretLevel());
        //标识已经成功

        BaseContextHandler.set("pm", pm);
        BaseContextHandler.set("user", user);
    }

    /**
     * 返回token中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(HttpServletRequest request, RequestContext ctx) throws Exception {
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        ctx.addZuulRequestHeader(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        return userAuthUtil.getInfoFromToken(authToken);
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private void setFailedRequest(String body, int code) {
        log.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.getResponse().setContentType("text/json;charset=UTF-8");
            ctx.setSendZuulResponse(false);
        }
    }

    /**
     * 优化查询该请求资源是否在用户所拥有的权限中
     *
     * @param ctx
     * @param user
     */
    private void checkUserPermission(String requestUri, List<FrontPermission> permissionInfos, RequestContext ctx, IJWTInfo user) {
        if (StringUtils.isEmpty(requestUri)) {
            throw new ClientInvalidException("requestUri Parameter exception...");
        }
        permissionInfos = permissionInfos.stream()
                .filter(new Predicate<FrontPermission>() {
                    @Override
                    public boolean test(FrontPermission permissionInfo) {
                        if (StringUtils.isEmpty(permissionInfo.getUri())) {
                            return false;
                        }
                        return requestUri.contains(permissionInfo.getUri());
                    }
                }).collect(Collectors.toList());

        if (permissionInfos.size() == 0) {
            BaseResponse tokenForbiddenResponse = new TokenForbiddenResponse("请求接口没有权限...");
            tokenForbiddenResponse.setStatus(CommonConstants.URL_NOT_PERMISSION);
            setFailedRequest(JSON.toJSONString(tokenForbiddenResponse), CommonConstants.HTTP_SUCCESS);
        }
        boolean anyMatch =
                permissionInfos.parallelStream()
                        .anyMatch(new Predicate<FrontPermission>() {
                            @Override
                            public boolean test(FrontPermission permissionInfo) {
                                return permissionInfo.getActionEntitySetList().stream().anyMatch(actionEntitySet ->
                                        ctx.getRequest().getMethod().equals(actionEntitySet.getMethod()));
                            }
                        });
        if (anyMatch) {
            //该用户有访问路径权限
            //实现一个策略取出来路径匹配度最高的，为确定权限
            FrontPermission max = Collections.max(permissionInfos);
            setCurrentUserInfoAndLog(ctx, user, max);
        } else {
            BaseResponse tokenForbiddenResponse = new TokenForbiddenResponse("请求接口操作没有权限...");
            tokenForbiddenResponse.setStatus(CommonConstants.URL_METHOD_NOT_PERMISSION);
            setFailedRequest(JSON.toJSONString(tokenForbiddenResponse), CommonConstants.HTTP_SUCCESS);
        }
    }

}
