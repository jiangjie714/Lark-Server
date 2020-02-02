package com.github.hollykunge.security.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 用户卡片实体类，供前端展示
 * @author zhhongyu
 * @since 2019-06-11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCardVO {
    private String id;

    private String title;

    private String url;

    private String i;

    private Integer position;

    private String description;

    private String icon;

    private String type;

    private Boolean defaultChecked;
}
