package com.github.hollykunge.security.auth.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "AUTH_CLIENT_SERVICE")
public class ClientService extends BaseEntity {

    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "DESCRIPTION")
    private String description;
}