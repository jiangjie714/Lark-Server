package com.github.hollykunge.security.simulation.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ActiveModels {
    private String modelName;
    private List<String> chargers;
}
