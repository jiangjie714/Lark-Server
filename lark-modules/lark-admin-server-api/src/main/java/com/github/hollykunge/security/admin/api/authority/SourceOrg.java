package com.github.hollykunge.security.admin.api.authority;


import lombok.Data;

import java.io.Serializable;

/**
 * @author fansq
 * @since 20-3-23
 * @deprecation 门户统计功能前端信息展示实体   饼图
 */
@Data
public class SourceOrg implements Serializable {
    /**
     * 单位名称
     */
    private String item;
    /**
     * 活动量
     */
    private Double count;
}
