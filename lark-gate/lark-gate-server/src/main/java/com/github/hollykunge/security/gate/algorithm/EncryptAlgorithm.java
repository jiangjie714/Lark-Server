package com.github.hollykunge.security.gate.algorithm;

/**
 * @author LARK
 */
public interface EncryptAlgorithm {

    /**
     * 加密
     * @param content 加密内容
     * @param encryptKey 加密Key
     * @return 加密内容
     * @throws Exception 加密失败
     */
    public String encrypt(String content, String encryptKey) throws Exception;

    /**
     * 解密
     * @param encryptStr 解密字符串
     * @param decryptKey 解密Key
     * @return 解密内容
     * @throws Exception 解密失败
     */
    public String decrypt(String encryptStr, String decryptKey) throws Exception;
}
