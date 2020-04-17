package com.github.hollykunge.security.simulation.vo;

import lombok.Data;

@Data
public class HistoryInfoVo {
    private double startTime;
    private double stopTime;
    private String name;
    private String topic;
    private String fileName;
}
