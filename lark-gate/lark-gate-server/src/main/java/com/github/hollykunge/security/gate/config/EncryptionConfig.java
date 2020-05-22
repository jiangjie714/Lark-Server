package com.github.hollykunge.security.gate.config;

import com.cxytiandi.encrypt.springboot.init.ApiEncryptDataInit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LARK
 */
@Component
@ConfigurationProperties(prefix = "spring.encrypt")
public class EncryptionConfig {

    /**
     * AES加密Key
     */
    private String key = "abcdef0123456789";

    /**
     * 需要对响应内容进行加密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> responseEncryptUriList = new ArrayList<String>();

    /**
     * 需要对请求内容进行解密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> requestDecryptUriList = new ArrayList<String>();

    /**
     * 忽略加密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> responseEncryptUriIgnoreList = new ArrayList<String>();

    /**
     * 忽略对请求内容进行解密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> requestDecryptUriIgnoreList = new ArrayList<String>();

    /**
     * 响应数据编码
     */
    private String responseCharset = "UTF-8";

    /**
     * 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景
     */
    private boolean debug = false;

    /**
     * 过滤器拦截模式
     */
    private String[] urlPatterns = new String[]{"/*"};

    /**
     * 过滤器执行顺序
     */
    private int order = 1;

    public EncryptionConfig() {
        super();
    }

    public EncryptionConfig(String key, List<String> responseEncryptUriList, List<String> requestDecryptUriList,
                            String responseCharset, boolean debug) {
        super();
        this.key = key;
        this.responseEncryptUriList = responseEncryptUriList;
        this.requestDecryptUriList = requestDecryptUriList;
        this.responseCharset = responseCharset;
        this.debug = debug;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getResponseEncryptUriList() {
        // 配置了注解则用注解获取的URI
        if (ApiEncryptDataInit.responseEncryptUriList.size() > 0) {
            return ApiEncryptDataInit.responseEncryptUriList;
        }
        return responseEncryptUriList;
    }

    public void setResponseEncryptUriList(List<String> responseEncryptUriList) {
        this.responseEncryptUriList = responseEncryptUriList;
    }

    public List<String> getRequestDecyptUriList() {
        // 配置了注解则用注解获取的URI
        if (ApiEncryptDataInit.requestDecyptUriList.size() > 0) {
            return ApiEncryptDataInit.requestDecyptUriList;
        }
        return requestDecryptUriList;
    }

    public void setRequestDecyptUriList(List<String> requestDecyptUriList) {
        this.requestDecryptUriList = requestDecyptUriList;
    }

    public String getResponseCharset() {
        return responseCharset;
    }

    public void setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setUrlPatterns(String[] urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public String[] getUrlPatterns() {
        return urlPatterns;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public List<String> getResponseEncryptUriIgnoreList() {
        // 配置了注解则用注解获取的URI
        if (ApiEncryptDataInit.responseEncryptUriIgnoreList.size() > 0) {
            return ApiEncryptDataInit.responseEncryptUriIgnoreList;
        }
        return responseEncryptUriIgnoreList;
    }

    public void setResponseEncryptUriIgnoreList(List<String> responseEncryptUriIgnoreList) {
        this.responseEncryptUriIgnoreList = responseEncryptUriIgnoreList;
    }

    public List<String> getRequestDecyptUriIgnoreList() {
        // 配置了注解则用注解获取的URI
        if (ApiEncryptDataInit.requestDecyptUriIgnoreList.size() > 0) {
            return ApiEncryptDataInit.requestDecyptUriIgnoreList;
        }
        return requestDecryptUriIgnoreList;
    }

    public void setRequestDecyptUriIgnoreList(List<String> requestDecyptUriIgnoreList) {
        this.requestDecryptUriIgnoreList = requestDecyptUriIgnoreList;
    }

    public List<String> getRequestDecyptParams(String uri) {
        List<String> params = ApiEncryptDataInit.requestDecyptParamMap.get(uri);
        if (CollectionUtils.isEmpty(params)) {
            return new ArrayList<>();
        }

        return params;
    }

}
