package com.github.hollykunge.security.portal.dto;

import lombok.Data;

/**
 * @author fansq
 * @since 19-12-23
 * @deprecation 卡片实体类
 */
@Data
public class CardDto {

    private String id;
    private String title;
    private String description;
    private String url;
    private String status;
    private String deleted;
    private String icon;
    private String type;
    private String orgCode;
    private String orgName;
    private String  cardOrgUserStatus;
}
