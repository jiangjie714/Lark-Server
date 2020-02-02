package com.github.hollykunge.security.admin.dictionary;

/**
 * @author: zhhongyu
 * @description: 组织机构级别
 * @since: Create in 10:09 2019/11/20
 */
public enum OrgLevelEnum {
    PARENT_DEPARTMENT("父级","4"),
    SECOND_DEPARTMENT("二级","2"),
    THIRD_PLACE("三级","3");
    private String name;
    private String value;
    OrgLevelEnum(String name, String value){
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
