package com.workhub.z.servicechat.config;

public enum ApiEnum {
    API_RESULT_SUCCESS("0",			"成功"),
    API_RESULT_FAIL("1","失败"),
    API_RESULT_ERROR("1000",			"异常");

    private String code;
    private String value;

    ApiEnum(String code,String value){
        this.code=code;
        this.value=value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
