package com.github.hollykunge.serviceunitproject.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.serviceunitproject.common.ProjectUnitTree;
import com.github.hollykunge.serviceunitproject.serviceimpl.ProjectUnitUserBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.github.hollykunge.serviceunitproject.service.ProjectUnitService;

import java.util.List;

@RestController
@RequestMapping(value = "project")
public class ProjectUnitController {

    @Autowired
    ProjectUnitService projectUnitService;

    /*
     * @RequestMapping 路由映射的注解
     * */
    @RequestMapping("/save")
    public  String saveProjectUnit(){
        System.out.println("导入项目单元开始........");
        projectUnitService.saveErbuProject();
        return "项目单元导入成功";
    }

   /* @RequestMapping("/getProjectUnit")
    public  String getProjectUnitJson(){
        System.out.println("获取项目单元开始........");
        projectUnitUserBiz.getProjectUnit("0eaea60b8ace4ca1a77ad1374ab2d890");
        return "获取项目成功";
    }*/

}
