package com.github.hollykunge.security.log.api.response;

public class LogBaseResponse {

    private int status = 200;
    private String message;
    private String timestamp = System.currentTimeMillis()+"";



    public LogBaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public LogBaseResponse() {
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
