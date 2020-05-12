package com.github.hollykunge.security.log.api.response;

public class LogObjectRestResponse<T> extends LogBaseResponse {

    T result;
    boolean rel;

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }


    public LogObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public LogObjectRestResponse data(T data) {
        this.setResult(data);
        return this;
    }

    public LogObjectRestResponse msg(String msg) {
        this.setMessage(msg);
        return this;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
