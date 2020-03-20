package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.entity.Simulation;
import com.github.hollykunge.security.mapper.SimulationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SimulationService extends BaseBiz<SimulationMapper, Simulation> {

    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    public List<Simulation> selectList(Simulation entity) {
        return mapper.userSimulations(entity);
    }
}
