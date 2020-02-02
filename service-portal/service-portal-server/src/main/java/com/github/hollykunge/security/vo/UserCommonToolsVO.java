package com.github.hollykunge.security.vo;

import lombok.Data;

/**
 * @author zhhongyu
 * 用户常用工具设置，前端展示实体类
 */
@Data
public class UserCommonToolsVO {
    private String id;
    private String uri;
    private String title;
    private String description;
    private Boolean defaultChecked;
}
