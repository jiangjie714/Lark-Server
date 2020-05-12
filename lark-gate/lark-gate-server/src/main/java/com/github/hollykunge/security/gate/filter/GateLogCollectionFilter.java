package com.github.hollykunge.security.gate.filter;

import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.auth.client.config.SysAuthConfig;
import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.context.BaseContextHandler;
import com.github.hollykunge.security.common.util.ClientUtil;
import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.gate.dto.LogInfoDto;
import com.github.hollykunge.security.gate.feign.ILarkSearchFeign;
import com.github.hollykunge.security.gate.utils.DBLog;
import com.github.hollykunge.security.gate.utils.GateLogUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.Objects;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-23 8:25
 */
@Component
@Slf4j
public class GateLogCollectionFilter extends ZuulFilter {

    @Autowired
    @Lazy
    private ILarkSearchFeign larkSearchFeign;
    @Autowired
    private GateLogUtils gateLogUtils;
    @Autowired
    private SysAuthConfig sysAuthConfig;

    @Override
    public String filterType() {
        return "post";
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
        int responseStatusCode = ctx.getResponseStatusCode();
        log.info("responseStatusCode: " + responseStatusCode);
        //获取内网网关ip地址
        String clientHost = (String) BaseContextHandler.get(CommonConstants.CLIENT_IP_ARG);
        String host = ClientUtil.getClientIp(ctx.getRequest());
        if(!StringUtils.isEmpty(clientHost)){
            host = clientHost;
        }
        FrontPermission pm = (FrontPermission) BaseContextHandler.get("pm");
        IJWTInfo user = (IJWTInfo) BaseContextHandler.get("user");
        String isSuccess = "1";
        if (responseStatusCode != CommonConstants.HTTP_SUCCESS) {
            isSuccess = "0";
        }
        //如果不是系统超级管理员，可以采集日志
        if(user != null && !Objects.equals(sysAuthConfig.getSysUsername(),user.getId())){
            transferContent(ctx,pm,user,host,isSuccess);
        }
        BaseContextHandler.remove();
        return null;
    }

    private void transferContent(RequestContext ctx,FrontPermission pm,IJWTInfo user,String host,String isSuccess){
        String opt = "";
        String optinfo = "";
        try{
            String methodCode = ctx.getRequest().getMethod();
            if("GET".equals(methodCode)){
                opt = "查询";
                optinfo = gateLogUtils.requestGet(ctx);
            }
            if("POST".equals(methodCode)){
                opt = "增加";
                optinfo = gateLogUtils.requestPost(ctx);
            }
            if("PUT".equals(methodCode)){
                opt = "修改";
                optinfo = gateLogUtils.requestPut(ctx);
            }
            if("DELETE".equals(methodCode)){
                opt = "删除";
                optinfo = gateLogUtils.requestDelete(ctx);
            }
            if (pm != null && user != null) {
                LogInfoDto logInfo = new LogInfoDto(pm.getTitle(),opt, ctx.getRequest().getRequestURI(),
                        new Date(), user.getId(), user.getName(), host, isSuccess,user.getUniqueName(),optinfo,user.getOrgPathCode());
                DBLog.getInstance().setLogService(larkSearchFeign).offerQueue(logInfo);
            }
        }catch (Exception e){
            log.info("日志采集失败...");
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            //有解析异常的情况下也进行日志采集，操作详细信息不进行记录
            optinfo = null;
            if (pm != null && user != null) {
                LogInfoDto logInfo = new LogInfoDto(pm.getTitle(),opt, ctx.getRequest().getRequestURI(),
                        new Date(), user.getId(), user.getName(), host, isSuccess,user.getUniqueName(),optinfo,user.getOrgPathCode());
                DBLog.getInstance().setLogService(larkSearchFeign).offerQueue(logInfo);
            }
        }
    }

}
