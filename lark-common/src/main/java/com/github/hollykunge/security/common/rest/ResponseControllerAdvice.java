package com.github.hollykunge.security.common.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hollykunge.security.common.exception.service.ServiceHandleException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.sun.xml.internal.ws.developer.ServerSideException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Lark-Server
 * @description: 全局响应数据处理
 * @author: Mr.Do
 * @create: 2020-04-22 13:07
 */
@ControllerAdvice(basePackages = "com.github.hollykunge.security.*.rest")
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型本身就是BaseResponse/ListRestResponse/ObjectRestResponse/TableResultResponse那就没有必要进行额外的操作，返回false
        return !returnType.getGenericParameterType().equals(BaseResponse.class) && !returnType.getGenericParameterType().equals(ListRestResponse.class) && !returnType.getGenericParameterType().equals(ObjectRestResponse.class) && !returnType.getGenericParameterType().equals(TableResultResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装，要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResultVO里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(new ObjectRestResponse<>().data(data).rel(true));
            } catch (JsonProcessingException e) {
                throw new ServiceHandleException(aClass.getName(), "ERROR LARK: Return type is error.");
            }
        }
        // 将原本的数据包装在ResultVO里
        return new ObjectRestResponse<>().data(data).rel(true);
    }
}
