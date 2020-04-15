package com.github.hollykunge.security.gate.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.gate.enums.ZuulErrorStatus;
import com.github.hollykunge.security.gate.utils.HttpGetRespouseUtil;
import com.google.common.collect.Lists;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author: zhhongyu
 * @description: 敏感词响应过滤
 * @since: Create in 13:19 2020/4/13
 */
@Slf4j
@Component
public class SensitiveFilter extends ZuulFilter {
    @Value("${sensitive.filter.data}")
    private String sensitiveAttr;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("待过滤数据的接口地址为:{}",ctx.getRequest().getRequestURI());
        //取出特殊的状态码，此列都为系统没有请求的数据，报的错误
        if(checkoutErrorStatus(ctx.getResponseStatusCode())){
            setNoDealLog();
            return null;
        }
        //当为获取数据的时候，解析响应体数据。过滤敏感json数据
        if (Objects.equals(HttpMethod.GET.name(), ctx.getRequest().getMethod())) {
            String responseBody = ctx.getResponseBody();
            //可能是网关忽略权限校验的请求地址，重新解析response
            if (StringUtils.isEmpty(responseBody)) {
                responseBody = HttpGetRespouseUtil.setResponse(ctx);
            }
            //如果为null或者空串则不用解析敏感数据
            if(StringUtils.isEmpty(responseBody)){
                setNoDealLog();
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(responseBody, JSONObject.class);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            //解析list或table的报文形式
            if (data != null) {
                JSONArray jsonArray = forEachJsonArray(data);
                result.put("data",jsonArray);
                jsonObject.put("result",result);
                ctx.setResponseBody(jsonObject.toJSONString());
                setLog();
                return null;
            }
            //解析object的报文形式
            if(result != null){
                removeObjSensitiveAttrs(result);
                setLog();
                return null;
            }
        }
        return null;
    }
    private boolean checkoutErrorStatus(Integer status){
        List<Integer> codes = ZuulErrorStatus.getCodes();
        return codes.contains(status);
    }
    /**
     * 循环json，删除敏感词汇字段
     */
    private JSONArray forEachJsonArray(JSONArray array){
        array.stream().forEach(json ->{
            if(json instanceof JSONObject){
                //table类型的列表
                JSONObject j = (JSONObject) json;
                removeObjSensitiveAttrs(j);
            }
            if(json instanceof JSONArray){
                //list类型的列表处理
                JSONArray j = (JSONArray) json;
                //递归去除敏感数据(适用于多层嵌套的list)
                forEachJsonArray(j);
            }
            //研讨服务有些返回list<String>的，特殊处理一下。string又不是jsonobj
            // 又不是jsonarray,也不用处理敏感数据了，一个string看不出来属性是啥东西
            if(json instanceof String){
                return;
            }
        });
        return array;
    }

    /**
     * 去除jsonobj类型的敏感数据
     * @param obj
     */
    private void removeObjSensitiveAttrs(JSONObject obj){
        List sensitiveAttrs = getSensitiveAttrs();
        sensitiveAttrs.forEach(attr ->{
            if(obj.containsKey(attr)){
                obj.remove(attr);
            }
        });
    }

    /**
     * 获取所有的敏感数据的属性变量名称
     * @return
     */
    private List getSensitiveAttrs(){
        if(StringUtils.isEmpty(sensitiveAttr)){
            return Lists.newArrayList();
        }
        String[] split = sensitiveAttr.split(",");
        return Arrays.asList(split);
    }

    private void setLog(){
        log.info("已完成敏感数据的过滤......");
    }

    private void setNoDealLog(){
        log.info("无需处理敏感数据...");
    }
}
