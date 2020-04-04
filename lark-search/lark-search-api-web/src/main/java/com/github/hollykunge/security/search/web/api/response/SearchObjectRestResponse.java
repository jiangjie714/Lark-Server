package com.github.hollykunge.security.search.web.api.response;

public class SearchObjectRestResponse<T> extends SearchBaseResponse {

    T result;
    boolean rel;

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }


    public SearchObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public SearchObjectRestResponse data(T data) {
        this.setResult(data);
        return this;
    }

    public SearchObjectRestResponse msg(String msg) {
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
