package com.github.hollykunge.security.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KnowledgeVO {

    private String id;

    private String type;

    private String name;

    private String date;

    private String color;

    private String time;
}
