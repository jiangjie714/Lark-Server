package com.github.hollykunge.security.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SimulationVO {
    private String id;

    private String user;

    private String type;

    private String name;

    private String content;

    private String percent;

    private String icon;

    private String tag;

    private boolean read;

    private String imageUrl;

    private String date;

    private Integer unreadCount;

    private String time;


}
