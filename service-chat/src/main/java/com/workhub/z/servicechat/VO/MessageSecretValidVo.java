package com.workhub.z.servicechat.VO;

/**
 * author:zhuqz
 * description:发送消息密级校验结果
 * date:2019/9/18 14:56
 **/
public class MessageSecretValidVo {
    //消息发送状态是否成功1是0否
    private String sendStatus;
    //如果消息发送成功，为空；如果消息发送失败，列出导致发送失败的涉密词汇，多个逗号分割
    private String secretWords;
    //如果消息发送成功，为空；如果消息发送失败，列出词汇密级：40秘密60机密，多个逗号分割，密级顺序与敏感词汇一一对应。
    private String secretLevels;

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSecretWords() {
        return secretWords;
    }

    public void setSecretWords(String secretWords) {
        this.secretWords = secretWords;
    }

    public String getSecretLevels() {
        return secretLevels;
    }

    public void setSecretLevels(String secretLevels) {
        this.secretLevels = secretLevels;
    }
}
