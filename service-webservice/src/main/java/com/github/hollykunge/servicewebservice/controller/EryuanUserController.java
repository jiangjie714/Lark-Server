package com.github.hollykunge.servicewebservice.controller;

import com.github.hollykunge.servicewebservice.config.Scheduled.OrgAndUserMqSendScheduled;
import com.github.hollykunge.servicewebservice.model.EryuanUser;
import com.github.hollykunge.servicewebservice.service.EryuanUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "eryuan")
@EnableAutoConfiguration
public class EryuanUserController {

    @Autowired
    EryuanUserService eryuanUserService;

    @Autowired
    OrgAndUserMqSendScheduled orgAndUserMqSendScheduled;

   // HttpServletResponse response;
    @RequestMapping(value = "/save")
public String saveEryuanUser(){
          System.out.println("开始执行导入........");
            eryuanUserService.saveEryuanUser();
            return "数据导入成功";

          }
    @GetMapping("/savexmlfile")
    public String saveOneXmlEryuanUser(){
        System.out.println("开始执行目录文件读取........");
        eryuanUserService.saveXmlFileEryuanUser();
        return "读文件目录的数据，导入成功";
    }

    @GetMapping("/changePath")
    public String changePath(){
        System.out.println("执行修改Path........");
        eryuanUserService.saveAllPath();
        return "修改path成功";
    }

    @GetMapping("/toMq")
    public String toMq(){
        System.out.println("执行发送Path至mq........");
        orgAndUserMqSendScheduled.refreshOrgMq();
        return "发送path成功";
    }

    @GetMapping("/test")
    public String reData(){
        System.out.println("发送到doc........");
        orgAndUserMqSendScheduled.refreshOnedocOrgMq();
        orgAndUserMqSendScheduled.refreshOnedocUserMq();
        return "发送doc成功";
    }
}
