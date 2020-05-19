package com.github.hollykunge.security.gate.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LARK
 */
@Data
@Validated
@NoArgsConstructor
@ConfigurationProperties(EncryptionConfig.PREFIX_ENCRYPT)
public class EncryptionConfig {
    public static final String PREFIX_ENCRYPT = "spring.encrypt";
    /**
     * AES加密Key
     */
    private String key = "abcdef0123456789";

    /**
     * 响应数据编码
     */
    private String responseCharset = "UTF-8";

    /**
     * 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景
     */
    private boolean debug = false;

    /**
     * 返回是否需要加密
     */
    private boolean encryptionResponse = false;

    /**
     * 过滤器拦截模式
     */
    private String[] urlPatterns = new String[]{"/*"};

    /**
     * 过滤器执行顺序
     */
    private int order = 2;

    /**
     * 需要过滤的方法
     */
    private List<String> methods = new ArrayList<String>(Arrays.asList("GET", "POST", "DELETE", "PUT"));

    public EncryptionConfig(String key, String responseCharset, boolean debug) {
        super();
        this.key = key;
        this.responseCharset = responseCharset;
        this.debug = debug;
    }
}
