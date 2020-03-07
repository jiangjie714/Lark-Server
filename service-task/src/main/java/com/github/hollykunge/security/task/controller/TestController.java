package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.task.entity.TaskDetails;
import com.github.hollykunge.security.task.service.impl.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/mongodb")
public class TestController {
    @Autowired
    private TestService testService;


    @RequestMapping("/findAll")
    @ResponseBody
    public List<TaskDetails> findDetails(){
        return testService.findDetails();
    }
}
