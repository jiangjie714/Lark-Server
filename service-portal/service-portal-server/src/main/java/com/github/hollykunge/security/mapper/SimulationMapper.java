package com.github.hollykunge.security.mapper;

import com.github.hollykunge.security.entity.Simulation;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SimulationMapper extends Mapper<Simulation> {
    List<Simulation> userSimulations(Simulation simulation);
}
