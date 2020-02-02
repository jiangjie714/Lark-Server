package com.workhub.z.servicechat.config;

import java.io.Serializable;
/**
*@Description: api返回结构体
*@Author: 忠
*@date: 2019/5/14
*/
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = -8046877952512659909L;
    private String code;
    private String massage;
    private double timestamp;
    private T data;

    public ApiResult(){
        this.code = "0";
        this.massage = "";
        this.timestamp = 0;
        this.data = null;
    }

    public ApiResult(String code,String massage,T data){
        this.code = code;
        this.massage = massage;
        this.timestamp = timestamp;
        this.data = data;
    }

    public ApiResult(String code,T data){
        this.code = code==null?"":code;
        if (code == null){
            this.massage = "";
        }else{
            for (ApiEnum fileToolCenterApiEnum: ApiEnum.values()) {
                if (fileToolCenterApiEnum.getCode().equals(code)){
                    this.massage = fileToolCenterApiEnum.getValue();
                    break;
                }
            }
        }
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
