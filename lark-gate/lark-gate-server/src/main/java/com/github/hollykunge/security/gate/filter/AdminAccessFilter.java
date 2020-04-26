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
import com.github.hollykunge.security.common.dictionary.HttpReponseStatusEnum;
import com.github.hollykunge.security.common.exception.server.ServerHandlerException;
import com.github.hollykunge.security.common.exception.service.PermissionException;
import com.github.hollykunge.security.common.exception.service.UserTokenException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.auth.TokenErrorResponse;
import com.github.hollykunge.security.common.msg.auth.TokenForbiddenResponse;
import com.github.hollykunge.security.common.util.ClientUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
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
    @Value("${intranet.header.dnname}")
    private String dnName;
    @Value("${intranet.header.clientip}")
    private String clientIp;

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
    private final String GATEWAY_ERROR = "网关发生异常";

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
        String errorMessage = null;
        int responStatus = HttpReponseStatusEnum.OK.value();
        try {
            BaseContextHandler.setToken(null);
            String dnname = request.getHeader(this.dnName);
            //获取内网网关地址
            String clientIp = request.getHeader(this.clientIp);
            BaseContextHandler.set(CommonConstants.CLIENT_IP_ARG, clientIp);
            //将院网关ip携带给云雀服务，供其他服务使用
            if (!StringUtils.isEmpty(clientIp)) {
                ctx.addZuulRequestHeader(this.clientIp, clientIp);
            }
            //正常用户名密码登录
            if (StringUtils.isEmpty(dnname)) {
                //此处使用null，正常用户名密码登录时，pid使用登录时的用户名
                return authorization(requestUri, ctx, request, null);
            }
            String pId = parsingDnname(dnname);
            //秘钥登录
            return authorization(requestUri, ctx, request, pId.toLowerCase());
        } catch (ServerHandlerException clientInvalidEx) {
            responStatus = HttpReponseStatusEnum.SYSTEM_ERROR.value();
            //client无效异常
            errorMessage =JSON.toJSONString(new BaseResponse(clientInvalidEx.getStatus(),GATEWAY_ERROR+clientInvalidEx.getMessage()));
        } catch (UserTokenException tokenEx) {
            responStatus = HttpReponseStatusEnum.BIZ_RUN_ERROR.value();
            //纯token异常
            errorMessage = JSON.toJSONString(new TokenErrorResponse(tokenEx.getMessage()));
        } catch (PermissionException permissionEx){
            responStatus = HttpReponseStatusEnum.BIZ_RUN_ERROR.value();
            //权限异常导致tokenerror
            TokenForbiddenResponse tokenForbiddenResponse = new TokenForbiddenResponse(permissionEx.getMessage());
            tokenForbiddenResponse.setStatus(permissionEx.getStatus());
            errorMessage = JSON.toJSONString(tokenForbiddenResponse);
        } catch (Exception ex){
            responStatus = HttpReponseStatusEnum.SYSTEM_ERROR.value();
            //其他未知异常
            errorMessage =JSON.toJSONString(new BaseResponse(CommonConstants.EX_OTHER_CODE,GATEWAY_ERROR+ExceptionUtils.getMessage(ex)));
        }
        //异常信息返回前端
        if(!StringUtils.isEmpty(errorMessage)){
            setFailedRequest(errorMessage, responStatus);
        }
        //无业务逻辑和异常逻辑，只是网关返回null，意为该过滤器已经执行完毕
        return null;
    }

    /**
     * 解析dnname成身份证号
     *
     * @param dnname
     * @return 身份证号
     */
    private String parsingDnname(String dnname) throws Exception {
        try {
            dnname = new String(dnname.getBytes(CommonConstants.PERSON_CHAR_SET));
        } catch (UnsupportedEncodingException e) {
            throw new ServerHandlerException("ERROR LARK: dnname transfer error, class=AdminAccessFilter.");
        }
        String[] userObjects = dnname.trim().split(",", 0);
        String pid = null;
        for (String val :
                userObjects) {
            val = val.trim();
            if (val.contains("t=") || val.contains("T=")) {
                pid = val.substring(2, val.length());
            }
        }
        return pid;
    }

    /**
     * 认证过程
     *
     * @param requestUri
     * @param ctx
     * @param request
     * @return
     */
    private Object authorization(String requestUri, RequestContext ctx, HttpServletRequest request, String pid) throws Exception {
        /**
         * 正常用户名密码登录
         */
        if (isStartWith(requestUri)) {
            if (!StringUtils.isEmpty(pid)) {
                ctx.addZuulRequestHeader("pid", pid);
            }
            if (request.getHeader(userAuthConfig.getTokenHeader()) != null) {
                //有token的也要校验一下token后再进行分发，否则不进行权限校验的，接口随便使用了
                getJWTUserAndsetPidHeader(request, ctx, pid);
                ctx.addZuulRequestHeader("token",
                        request.getHeader(userAuthConfig.getTokenHeader()));
            }
            return null;
        }
        /**
         * 校验权限的接口实现
         */
        IJWTInfo user = getJWTUserAndsetPidHeader(request, ctx, pid);
        //证明并未解析到user，token有问题，不能继续了，驳回请求
        //如果为超级管理员，则直接通过
        if (Objects.equals(user.getUniqueName(), sysAuthConfig.getSysUsername())) {
            this.setCurrentUserInfoAndLog(ctx, user, null);
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

    private void setZuulHeaderPid(RequestContext ctx, String pid, String userName) {
        //将pid放在请求头中
        if (StringUtils.isEmpty(pid)) {
            pid = userName;
        }
        //将pid放置在请求头中
        ctx.addZuulRequestHeader("pid", pid);
    }

    /**
     * 在上下文中设置当前用户信息和操作日志
     */
    private void setCurrentUserInfoAndLog(RequestContext ctx, IJWTInfo user, FrontPermission pm) throws Exception {
        String host = ClientUtil.getClientIp(ctx.getRequest());
        ctx.addZuulRequestHeader("userId", user.getId());
        ctx.addZuulRequestHeader("userName", URLEncoder.encode(user.getName(), "UTF-8"));
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
    private IJWTInfo getJWTUserAndsetPidHeader(HttpServletRequest request, RequestContext ctx, String pid) throws Exception {
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        ctx.addZuulRequestHeader(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        IJWTInfo user = userAuthUtil.getInfoFromToken(authToken);
        //设置网关请求头pid
        setZuulHeaderPid(ctx, pid, user.getUniqueName());
        return user;
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
    private void checkUserPermission(String requestUri, List<FrontPermission> permissionInfos, RequestContext ctx, IJWTInfo user) throws Exception {
        permissionInfos = permissionInfos.stream()
                .filter((FrontPermission permissionInfo) ->{
                        if (StringUtils.isEmpty(permissionInfo.getUri())) {
                            return false;
                        }
                        return requestUri.contains(permissionInfo.getUri());
                }).collect(Collectors.toList());

        if (permissionInfos.size() == 0) {
            throw new PermissionException(CommonConstants.URL_NOT_PERMISSION,"资源请求失败，没有请求该资源的权限。");
        }
        boolean anyMatch =
                permissionInfos.parallelStream()
                        .anyMatch((FrontPermission permissionInfo) -> permissionInfo.getActionEntitySetList().stream().anyMatch(actionEntitySet ->
                                        ctx.getRequest().getMethod().equals(actionEntitySet.getMethod())));
        if (anyMatch) {
            //该用户有访问路径权限
            //实现一个策略取出来路径匹配度最高的，为确定权限
            FrontPermission max = Collections.max(permissionInfos);
            setCurrentUserInfoAndLog(ctx, user, max);
        } else {
            throw new PermissionException(CommonConstants.URL_METHOD_NOT_PERMISSION,"资源请求失败，没有操作该资源的权限。");
        }
    }

}
