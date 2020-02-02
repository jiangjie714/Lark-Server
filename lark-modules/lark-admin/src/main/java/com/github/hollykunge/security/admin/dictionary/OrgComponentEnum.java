package com.github.hollykunge.security.admin.dictionary;

/**
 * @author: zhhongyu
 * @description: 组件类型
 * @since: Create in 10:09 2019/11/20
 */
public enum OrgComponentEnum {
    WITHIN_DEPARTMENT("部门内","0"),
    OUTSIDE_DEPARTMENT("跨部门","1"),
    OUTSIDE_PLACE("跨场所","2");
    private String name;
    private String value;
    OrgComponentEnum(String name,String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
