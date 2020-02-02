package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 工作热力图
 *
 * @author: holly
 * @since: 2019/6/17
 */
@Data
@Table(name = "PORTAL_HEATMAP")
public class HeatMap {
    @Column(name = "ID")
    @Id
    private String id;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "MAP_DATE")
    private Date mapDate;
    @Column(name = "COMMITS")
    private Integer commits;
    @Transient
    private Integer month;
    @Transient
    private Integer day;
    @Transient
    private String week;
    @Transient
    private String date;
}
