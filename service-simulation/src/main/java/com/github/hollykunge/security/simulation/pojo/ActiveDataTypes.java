package com.github.hollykunge.security.simulation.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ActiveDataTypes {
    private Long key;
    private String name;
    private String description;
    private Long currentCount;
    private List<Content> content;
}
