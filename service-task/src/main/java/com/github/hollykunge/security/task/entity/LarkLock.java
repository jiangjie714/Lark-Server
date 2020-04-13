package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * @author  fansq
 * @since 20-4-13
 * @deprecation
 */
@Data
@Table(name = "LARK_LOCK")
public class LarkLock extends BaseEntity {

    @Column(name = "VALUE")
    private Short value;

    @Column(name = "EXPIRETIME")
    private Long expireTime;

}