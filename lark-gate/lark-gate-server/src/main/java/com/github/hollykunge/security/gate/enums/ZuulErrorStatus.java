package com.github.hollykunge.security.gate.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 17:19 2020/4/13
 */
public enum ZuulErrorStatus {
    SERVER_INVALID("服务不可用",503),
    SERVER_ERROR("服务器内部错误",500);

    private String name;
    private int code;

    ZuulErrorStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public static List<Integer> getCodes() {
        List result = Lists.newArrayList();
        ZuulErrorStatus[] values = ZuulErrorStatus.values();
        for (ZuulErrorStatus status:
                values) {
            int code = status.getCode();
            result.add(code);
        }
        return result;
    }
    public int getCode(){
        return this.code;
    }
}
