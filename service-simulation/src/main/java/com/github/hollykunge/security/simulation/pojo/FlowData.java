package com.github.hollykunge.security.simulation.pojo;

import lombok.Data;

import java.util.List;

@Data
public class FlowData {
    private List<Nodes> nodes;
    private List<Edges> edges;
}
