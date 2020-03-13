package com.github.hollykunge.security.gate.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.ClientInvalidException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * @author: zhhongyu
 * @description: 日志处理request工具类
 * @since: Create in 10:13 2019/9/11
 */
@Component
@Slf4j
public class GateLogUtils {
    @Value("${zuul.prefix}")
    private String zuulPrefix;

    public String requestGet(RequestContext ctx) throws Exception {
        String optInfo = null;
        String requestURI = checkFunctionParam(ctx);
        log.info("请求方式:GET");
        log.info("请求uri为：" + requestURI);
        //全部数据
        if (requestURI.toLowerCase().contains(CommonConstants.GET_GATE_LOG_REQUEST_LIST)) {
            JSONObject respones = resolvResponse(ctx);
            //此处判断不为空，供网关服务降级时
            if (respones != null) {
                String count = respones.getJSONObject("result").getString("count");
                String opt = resolvUri(requestURI);
                optInfo = "查询" + opt + count + " 条";
                return optInfo;
            }
            String opt = resolvUri(requestURI);
            return opt;
        }
        //分页
        if (requestURI.toLowerCase().contains(CommonConstants.GET_GATE_LOG_REQUEST_PAGE)) {
            JSONObject respones = resolvResponse(ctx);
            if (respones != null) {
                String pageNo = respones.getJSONObject("result").getString("pageNo");
                String pageSize = respones.getJSONObject("result").getString("pageSize");
                String opt = resolvUri(requestURI);
                optInfo = opt + "查询列表集第 " + pageNo + " 页中 " + pageSize + " 条数据";
                return optInfo;
            }
            String opt = resolvUri(requestURI);
            return opt;
        }
        //导出
        if (requestURI.toLowerCase().contains(CommonConstants.GET_GATE_LOG_REQUEST_EXPORT)) {
            String opt = resolvUri(requestURI);
            return opt;
        }
        //剩下的查询单个
        JSONObject respones = resolvResponse(ctx);
        if (respones != null) {
            respones = respones.getJSONObject("result");
            String name = getName(respones);
            String opt = resolvUri(requestURI);
            return "查询" + opt + name;
        }
        return "查询信息";
    }

    public String requestPost(RequestContext ctx) throws Exception {
        String requestUri = checkFunctionParam(ctx);
        log.info("请求方式:POST");
        log.info("请求uri为：" + requestUri);
        JSONObject jsonObject = resolvRequest(ctx);
        String opt = resolvUri(requestUri);
        return "新增" + opt + getName(jsonObject);
    }

    public String requestPut(RequestContext ctx) throws Exception {
        String requestURI = checkFunctionParam(ctx);
        log.info("请求方式:PUT");
        log.info("请求uri为：" + requestURI);
        int count = requestURI.split("/").length;
        //为一级uri
//        if (count == 2) {
        JSONObject jsonObject = resolvRequest(ctx);
//        String name = getName(jsonObject);
        String opt = resolvUri(requestURI);
        String jsonBody = "";
        if(jsonObject != null){
            jsonBody = jsonObject.toJSONString();
        }
        return "修改" + opt + jsonBody;
//        }
        //为2级uri
//        if (count == 3) {
//            String opt = resolvUri(requestURI);
//            return opt;
//        }
//        return "";
    }

    public String requestDelete(RequestContext ctx) throws Exception {
        String requestUri = checkFunctionParam(ctx);
        JSONObject response = resolvResponse(ctx);
        if (response != null) {
            JSONObject responseBody = response.getJSONObject("result");
            String name = getName(responseBody);
            String opt = resolvUri(requestUri);
            return opt + "删除" + name;
        }
        return "删除信息";
    }

    public String getName(JSONObject jsonObject) {
        if (jsonObject == null) {
            return "";
        }
        String[] nameArray = CommonConstants.GET_GATE_LOG_REQUEST_BODY_RULE.split(",");
        if (nameArray.length == 0) {
            throw new BaseException("获取requestbody实体类基本参数规则没有在常量池中定义...");
        }
        String result = "";
        for (String name :
                nameArray) {
            if (jsonObject.containsKey(name)) {
                result = jsonObject.getString(name);
                break;
            }
        }
        return result;
    }

    private String checkFunctionParam(RequestContext ctx) {
        if (ctx == null) {
            throw new ClientInvalidException("request must be not null...");
        }
        String requestURI = ctx.getRequest().getRequestURI();
        requestURI = requestURI.substring(zuulPrefix.length() + 1, requestURI.length());
        requestURI = requestURI.substring(requestURI.indexOf("/"), requestURI.length());
        if (StringUtils.isEmpty(requestURI)) {
            throw new ClientInvalidException("request no request uri .. ");
        }
        return requestURI;
    }

    private JSONObject resolvResponse(RequestContext ctx) throws IOException {
        InputStream stream = ctx.getResponseDataStream();
        String body = IOUtils.toString(stream);
        RequestContext.getCurrentContext().setResponseBody(body);
        if (!StringUtils.isEmpty(body)) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(body, JSONObject.class);
                //子服务正常响应
                if (jsonObject.getIntValue("status") == 200) {
                    return jsonObject;
                }
            } catch (Exception e) {
                log.error("解析响应体异常..原因:{}", e.getMessage());
            }
        }
        return null;
    }

    private JSONObject resolvRequest(RequestContext ctx) throws IOException {
        String bodyTemp = ctx.getRequest().getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (!StringUtils.isEmpty(bodyTemp)) {
            bodyTemp = bodyTemp.toLowerCase();
            try {
                JSONObject json = JSONObject.parseObject(bodyTemp, JSONObject.class);
                return json;
            } catch (Exception e) {
                log.error("解析request请求体异常...原因:{}", e.getMessage());
            }
        }
        return null;
    }

    public String resolvUri(String requestUri) {
        //先解析特殊接口
        if (requestUri.contains(CommonConstants.GET_GATE_LOG_REQUEST_FEIGN)) {
            requestUri = requestUri.substring(0, CommonConstants.GET_GATE_LOG_REQUEST_FEIGN.length());
            return GateLogRestOptionEnum.getEnumByValue(CommonConstants.GET_GATE_LOG_REQUEST_FEIGN).getName();
        }
        String[] requestArr = null;
        if (requestUri.contains("/")) {
            requestUri = requestUri.substring(1, requestUri.length());
            requestArr = requestUri.split("/");
        }
        if (requestArr == null) {
            return "";
        }
        if (requestArr.length == 1) {
            return GateLogRestOptionEnum.getEnumByValue(requestArr[0]).getName();
        }
        if (requestArr.length == 2) {
            return GateLogRestOptionEnum.getEnumByValue(requestArr[0]).getName()
                    + GateLogRestOptionEnum.getEnumByValue(requestArr[1]).getName();
        }
        return "";
    }
}
