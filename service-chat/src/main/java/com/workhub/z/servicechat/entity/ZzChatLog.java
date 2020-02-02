package com.workhub.z.servicechat.entity;

import java.io.Serializable;
import java.util.Date;

public class ZzChatLog  implements Serializable {

    private static final long serialVersionUID = -8398963698576559579L;
    //id
    private String id;
    //用户id
    private String userId;
    //用户名称
    private String userName;
    //用户ip
    private String userIp;
    //访问url
    private String url;
    //访问的类和方法
    private String method;
    //参数
    private String parameters;
    //返回结果
    private String returnResult;
    //是否成功 1是0否
    private String successFlg;
    //如果失败返回的异常信息
    private String errorInf;
    //时间戳
    private Date time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(String returnResult) {
        this.returnResult = returnResult;
    }

    public String getSuccessFlg() {
        return successFlg;
    }

    public void setSuccessFlg(String successFlg) {
        this.successFlg = successFlg;
    }

    public String getErrorInf() {
        return errorInf;
    }

    public void setErrorInf(String errorInf) {
        this.errorInf = errorInf;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
