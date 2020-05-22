package com.github.hollykunge.security.gate.filter;


import com.github.hollykunge.security.gate.algorithm.AesEncryptAlgorithm;
import com.github.hollykunge.security.gate.algorithm.EncryptAlgorithm;
import com.github.hollykunge.security.gate.config.EncryptionConfig;
import com.github.hollykunge.security.gate.wrapper.EncryptionResponseWrapper;
import com.github.hollykunge.security.gate.wrapper.EncryptionReqestWrapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LARK
 */
@Component
@Slf4j
public class SecurityFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private EncryptionConfig encryptionConfig;
    private EncryptAlgorithm encryptAlgorithm = new AesEncryptAlgorithm();

    public SecurityFilter() {
        this.encryptionConfig = new EncryptionConfig();
    }

    public SecurityFilter(EncryptionConfig config) {
        this.encryptionConfig = config;
    }

    public SecurityFilter(EncryptionConfig config, EncryptAlgorithm encryptAlgorithm) {
        this.encryptionConfig = config;
        this.encryptAlgorithm = encryptAlgorithm;
    }

    public SecurityFilter(String key) {
        EncryptionConfig config = new EncryptionConfig();
        config.setKey(key);
        this.encryptionConfig = config;
    }

    public SecurityFilter(String key, List<String> responseEncryptUriList, List<String> requestDecyptUriList,
                            String responseCharset, boolean debug) {
        this.encryptionConfig = new EncryptionConfig(key, responseEncryptUriList, requestDecyptUriList, responseCharset, debug);
    }
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        HttpServletResponse resp = ctx.getResponse();

        String uri = req.getRequestURI();
        logger.debug("RequestURI: {}", uri);

        // 调试模式不加解密
        if (encryptionConfig.isDebug()) {
            return null;
        }

        boolean decryptionStatus = this.contains(encryptionConfig.getRequestDecyptUriList(), uri, req.getMethod());
        boolean encryptionStatus = this.contains(encryptionConfig.getResponseEncryptUriList(), uri, req.getMethod());
        boolean decryptionIgnoreStatus = this.contains(encryptionConfig.getRequestDecyptUriIgnoreList(), uri, req.getMethod());
        boolean encryptionIgnoreStatus = this.contains(encryptionConfig.getResponseEncryptUriIgnoreList(), uri, req.getMethod());

        // 没有配置具体加解密的URI默认全部都开启加解密
        if (CollectionUtils.isEmpty(encryptionConfig.getRequestDecyptUriList())
                && CollectionUtils.isEmpty(encryptionConfig.getResponseEncryptUriList())) {
            decryptionStatus = true;
            encryptionStatus = true;
        }

        // 接口在忽略加密列表中
        if (encryptionIgnoreStatus) {
            encryptionStatus = false;
        }

        // 接口在忽略解密列表中
        if (decryptionIgnoreStatus) {
            decryptionStatus = false;
        }

        // 没有加解密操作
        if (!decryptionStatus && !encryptionStatus) {
            return null;
        }

        EncryptionResponseWrapper responseWrapper = null;
        EncryptionReqestWrapper requestWrapper = null;
        // 配置了需要解密才处理
        if (decryptionStatus) {
            requestWrapper = new EncryptionReqestWrapper(req);
            processDecryption(requestWrapper, req);
        }

        if (encryptionStatus) {
            responseWrapper = new EncryptionResponseWrapper(resp);
        }

        // 同时需要加解密
        if (encryptionStatus && decryptionStatus) {
            ctx.setRequest(requestWrapper);
            ctx.setResponse(responseWrapper);
            return null;
        } else if (encryptionStatus) {
            //只需要响应加密
            ctx.setResponse(responseWrapper);
            return null;
        } else if (decryptionStatus) {
            ctx.setRequest(requestWrapper);
            return null;
        }

        // 配置了需要加密才处理
        if (encryptionStatus) {
            String responseData = responseWrapper.getResponseData();
            writeEncryptContent(responseData, resp);
        }
        return null;
    }

    /**
     * 请求解密处理
     *
     * @param requestWrapper
     * @param req
     */
    private void processDecryption(EncryptionReqestWrapper requestWrapper, HttpServletRequest req) {
        String requestData = requestWrapper.getRequestData();
        String uri = req.getRequestURI();
        logger.debug("RequestData: {}", requestData);
        try {
            String decryptRequestData = encryptAlgorithm.decrypt(requestData, encryptionConfig.getKey());
            logger.debug("DecryptRequestData: {}", decryptRequestData);
            requestWrapper.setRequestData(decryptRequestData);

            // url参数解密
            Map<String, String> paramMap = new HashMap<>();
            Enumeration<String> parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String prefixUri = req.getMethod().toLowerCase() + ":" + uri;
                if (encryptionConfig.getRequestDecyptParams(prefixUri).contains(paramName)) {
                    String paramValue = req.getParameter(paramName);
                    String decryptParamValue = encryptAlgorithm.decrypt(paramValue, encryptionConfig.getKey());
                    paramMap.put(paramName, decryptParamValue);
                }
            }
            requestWrapper.setParamMap(paramMap);
        } catch (Exception e) {
            logger.error("请求数据解密失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出加密内容
     *
     * @param responseData
     * @param response
     * @throws IOException
     */
    private void writeEncryptContent(String responseData, HttpServletResponse response) throws IOException {
        logger.debug("ResponseData: {}", responseData);
        ServletOutputStream out = null;
        try {
            responseData = encryptAlgorithm.encrypt(responseData, encryptionConfig.getKey());
            logger.debug("EncryptResponseData: {}", responseData);
            response.setContentLength(responseData.length());
            response.setCharacterEncoding(encryptionConfig.getResponseCharset());
            out = response.getOutputStream();
            out.write(responseData.getBytes(encryptionConfig.getResponseCharset()));
        } catch (Exception e) {
            logger.error("响应数据加密失败", e);
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    private boolean contains(List<String> list, String uri, String methodType) {
        if (list.contains(uri)) {
            return true;
        }
        String prefixUri = methodType.toLowerCase() + ":" + uri;
        logger.debug("contains uri: {}", prefixUri);
        return list.contains(prefixUri);
    }
}
