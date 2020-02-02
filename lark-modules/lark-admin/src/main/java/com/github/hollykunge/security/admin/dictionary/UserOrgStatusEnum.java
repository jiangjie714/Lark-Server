package com.github.hollykunge.security.admin.dictionary;

/**
 * @author: zhhongyu
 * @description: 人员或组织是否删除状态
 * @since: Create in 11:19 2019/12/2
 */
public enum UserOrgStatusEnum {
    DELETED("删除","2");
    private String name;
    private String value;
    UserOrgStatusEnum(String name, String value){
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
