package com.github.hollykunge.security.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LinkVO {
    private String id;

    private String name;

    private String link;

    private String img;

    private String description;

    private String date;

    private String time;
}
