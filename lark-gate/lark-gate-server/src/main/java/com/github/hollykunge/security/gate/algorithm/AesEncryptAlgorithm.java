package com.github.hollykunge.security.gate.algorithm;


import com.github.hollykunge.security.gate.utils.AesEncryptUtils;

/**
 * @author LARK
 */
public class AesEncryptAlgorithm implements EncryptAlgorithm {
    @Override
    public String encrypt(String content, String encryptKey) throws Exception {
        return AesEncryptUtils.aesEncrypt(content, encryptKey);
    }

    @Override
    public String decrypt(String encryptStr, String decryptKey) throws Exception {
        return AesEncryptUtils.aesDecrypt(encryptStr, decryptKey);
    }
}
