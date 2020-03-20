package com.github.hollykunge.security.search.ik.dto;

import lombok.Data;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:23 2020/3/18
 */
@Data
public class OrgDto {
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 父级组织ID
     */
    private String parentId;
    /**
     * 组织层级（几级单位）
     */
    private Integer orgLevel;
    /**
     * 保密资格等级
     */
    private String orgSecret;
    /**
     * 组织别名
     */
    private String externalName;
    /**
     * 排序号
     */
    private Long orderId;
    /**
     * 是否删除，0为false，1为true
     */
    private String deleted;
    /**
     * 描述
     */
    private String description;

    /**
     * 组织code
     */
    private String orgCode;

    /**
     * 组织层级关系code
     */
    private String pathCode;

    /**
     * 组织层级关系name
     */
    private String pathName;
}
