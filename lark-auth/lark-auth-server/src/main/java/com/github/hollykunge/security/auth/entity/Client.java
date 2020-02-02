package com.github.hollykunge.security.auth.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "AUTH_CLIENT")
public class Client extends BaseEntity {
    @Column(name = "CODE")
    private String code;

    @Column(name = "SECRET")
    private String secret;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LOCKED")
    private String locked;

    @Column(name = "DESCRIPTION")
    private String description;
}