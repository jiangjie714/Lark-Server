package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @description: 卡片设置- 用户卡片表
 * @author zhhongyu
 * @since 2019年06月1日
 */
@Data
@Table(name = "PORTAL_USER_CARD")
public class UserCard {

    @Column(name = "ID")
    @Id
    private String id;

    @Column(name = "USER_ID ")
    private String userId;

    @Column(name = "CARD_ID")
    private String cardId;

    @Column(name = "STATUS")
    private String status;
    /**
     * 第几个卡片
     */
    @Column(name = "I")
    private String i;

    @Column(name = "POSITION")
    private Integer position;
}
