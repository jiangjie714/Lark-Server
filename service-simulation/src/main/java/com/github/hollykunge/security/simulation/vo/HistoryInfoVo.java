package com.github.hollykunge.security.simulation.vo;

import com.github.hollykunge.security.simulation.pojo.HistoryTopics;
import lombok.Data;

import java.util.List;

@Data
public class HistoryInfoVo {

    private double startTime;

    private double stopTime;

    /**
     * collection名
     */
    private String name;

    private String fileName;

    private List<HistoryTopics> historyTopics;
}
