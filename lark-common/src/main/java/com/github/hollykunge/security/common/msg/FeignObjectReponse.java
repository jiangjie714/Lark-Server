package com.github.hollykunge.security.common.msg;

import org.apache.poi.ss.formula.functions.T;

/**
 * @author: zhhongyu
 * @description: 全局feign统一object类型
 * @since: Create in 10:41 2020/4/28
 */
public class FeignObjectReponse extends BaseResponse{
    T result;
    boolean rel;

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }

    public FeignObjectReponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public FeignObjectReponse data(T data) {
        this.setResult(data);
        return this;
    }

    public FeignObjectReponse msg(String msg) {
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
