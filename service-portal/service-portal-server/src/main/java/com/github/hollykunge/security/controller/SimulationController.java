package com.github.hollykunge.security.controller;

import com.alibaba.druid.util.StringUtils;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.Simulation;
import com.github.hollykunge.security.service.SimulationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("showSimulation")
public class SimulationController extends BaseController<SimulationService, Simulation> {
    @RequestMapping(value = "/userSimulation", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Simulation>> userMessage() {
        String userID = request.getHeader("userId");
        if (StringUtils.isEmpty(userID)) {
            throw new BaseException("request contains no user...");
        }
        Simulation simulation = new Simulation();
        simulation.setUserId(userID);
        List<Simulation> simulations = baseBiz.selectList(simulation);
        return new ListRestResponse("", simulations.size(), simulations);
    }
}
