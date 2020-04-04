package com.github.hollykunge.security.simulation.pojo;

import lombok.Data;

@Data
public class Edges {
    private String shape;
    private String source;
    private Long sourceAnchor;
    private String target;
    private Long targetAnchor;
    private String id;
    private Long index;
}
