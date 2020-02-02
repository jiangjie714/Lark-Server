package com.github.hollykunge.security.admin.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "ADMIN_ORGUSERMAP")
public class OrgUserMap extends BaseEntity {

    @Column(name = "ORG_ID")
    private String orgId;

    @Column(name = "USER_ID")
    private String userId;

}