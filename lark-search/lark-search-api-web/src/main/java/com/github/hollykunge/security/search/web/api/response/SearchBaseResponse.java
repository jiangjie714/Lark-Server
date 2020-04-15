package com.github.hollykunge.security.search.web.api.response;

public class SearchBaseResponse {

    private int status = 200;
    private String message;
    private String timestamp = System.currentTimeMillis()+"";



    public SearchBaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public SearchBaseResponse() {
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
