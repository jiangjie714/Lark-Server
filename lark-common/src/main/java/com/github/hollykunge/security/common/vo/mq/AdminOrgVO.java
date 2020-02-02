package com.github.hollykunge.security.common.vo.mq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * admin组织mq实体
 * @Author: yzq
 * @Date: 创建于 2019/7/22 12:39
 */
@Data
@NoArgsConstructor
@ToString
public class AdminOrgVO implements Serializable {

    private static final long serialVersionUID = 3215731879666698331L;

    /**
     * -->CASIC_ORG_CODE
     */
    private String id;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 父级组织ID-->CASIC_PORG_CODE
     */
    private String parentId;
    /**
     * 组织层级（几级单位）
     */
    private Integer orgLevel;
    /**
     * 保密资格等级-->CASIC_ORG_SECRET
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
     * 是否删除，0新增，1修改，2删除-->SYNC_TYPE
     */
    private String deleted;
    /**
     * 描述-->REMARK
     */
    private String description;

    /**
     * 组织code-->CASIC_ORG_CODE
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
