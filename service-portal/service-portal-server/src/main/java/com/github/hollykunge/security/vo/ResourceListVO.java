package com.github.hollykunge.security.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResourceListVO {
    private String type;

    private String name;

    private String img;

    private String date;

    private String time;
}
