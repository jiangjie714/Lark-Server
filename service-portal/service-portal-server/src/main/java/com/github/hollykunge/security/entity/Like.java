package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName Like
 * @Description 问题或评论点赞
 * @Author hollykunge
 * @Date 2019/6/30 13:17
 * @Version 1.0
 **/
@Data
@Table(name = "PORTAL_NAME")
public class Like {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "USER_ID")
    private String userId;
    /**
     * 点赞实体id
     */
    @Column(name = "ENTITY_ID")
    private String entityId;
}
