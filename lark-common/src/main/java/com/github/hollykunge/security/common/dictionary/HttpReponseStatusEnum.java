package com.github.hollykunge.security.common.dictionary;

/**
 * @author: zhhongyu
 * @description: http响应状态
 * @since: Create in 15:56 2019/12/3
 */
public enum HttpReponseStatusEnum {
    OK(200, "请求成功"),
    REDIRECT(300,"重定向"),
    BIZ_RUN_ERROR(400,"业务运行时异常响应"),
    SYSTEM_ERROR(500,"系统错误");
    private final int value;
    private final String reasonPhrase;

    private HttpReponseStatusEnum(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }
    public int value() {
        return this.value;
    }
}
