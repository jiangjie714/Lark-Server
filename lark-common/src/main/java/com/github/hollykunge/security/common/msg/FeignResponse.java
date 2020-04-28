package com.github.hollykunge.security.common.msg;

/**
 * @author: zhhongyu
 * @description: feign统一返回
 * @since: Create in 10:46 2020/4/28
 */
public class FeignResponse {
    private int status = 200;
    private String message;
    private String timestamp = System.currentTimeMillis()+"";



    public FeignResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public FeignResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
