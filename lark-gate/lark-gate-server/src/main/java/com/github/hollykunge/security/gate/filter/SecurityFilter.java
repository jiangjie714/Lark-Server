package com.github.hollykunge.security.gate.filter;

import com.github.hollykunge.security.gate.config.EncryptionConfig;
import com.github.hollykunge.security.gate.utils.algorithm.AesEncryptAlgorithm;
import com.github.hollykunge.security.gate.utils.algorithm.EncryptAlgorithm;

import com.github.hollykunge.security.gate.wrapper.EncryptionRequestWrapper;
import com.github.hollykunge.security.gate.wrapper.EncryptionResponseWrapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author LARK
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private final EncryptAlgorithm encryptAlgorithm = new AesEncryptAlgorithm();

    private final EncryptionConfig encryptionConfig;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
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

        // 调试模式不加解密，默认是false
        if (encryptionConfig.isDebug()) {
            return null;
        }

        EncryptionResponseWrapper responseWrapper = null;
        EncryptionRequestWrapper requestWrapper = null;

        // 判断方法是否需要解密，默认是GET/POST/DELETE/PUT
        if (encryptionConfig.getMethods().contains(req.getMethod())) {
            requestWrapper = new EncryptionRequestWrapper(req);
            // 开始解密处理
            processDecryption(requestWrapper, req);
            ctx.setRequest(requestWrapper);
        }
        // 返回是否需要加密，默认不加密
        if (encryptionConfig.isEncryptionResponse()) {
            responseWrapper = new EncryptionResponseWrapper(resp);
            String responseData = responseWrapper.getResponseData();
            writeEncryptContent(responseData, resp);
            ctx.setResponse(responseWrapper);
        }

        return null;
    }

    /**
     * 请求解密处理
     *
     * @param requestWrapper
     */
    private void processDecryption(EncryptionRequestWrapper requestWrapper, HttpServletRequest req) {
        String requestData = requestWrapper.getRequestData();
        logger.debug("RequestData: {}", requestData);
        try {
            String decryptRequestData = encryptAlgorithm.decrypt(requestData, encryptionConfig.getKey());
            logger.debug("DecryptRequestData: {}", decryptRequestData);
            requestWrapper.setRequestData(decryptRequestData);
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

//    private boolean contains(List<String> list, String uri, String methodType) {
//        if (list.contains(uri)) {
//            return true;
//        }
//        String prefixUri = methodType.toLowerCase() + ":" + uri;
//        logger.debug("contains uri: {}", prefixUri);
//        return list.contains(prefixUri);
//    }
}
