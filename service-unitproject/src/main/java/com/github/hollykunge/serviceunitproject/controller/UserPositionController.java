package com.github.hollykunge.serviceunitproject.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.serviceunitproject.common.ObjectRestResponse;
import com.github.hollykunge.serviceunitproject.common.UserInfo;
import com.github.hollykunge.serviceunitproject.service.UserPositionService;
import com.github.hollykunge.serviceunitproject.serviceimpl.UserPositionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "project")
public class UserPositionController  {

    @Autowired
    UserPositionService userPositionService;

    /*
     * @RequestMapping 路由映射的注解
     * */
    @RequestMapping("/positionuser")
    public  ObjectRestResponse getProjectUnit(@RequestParam("userID") String userID){
        UserInfo userInfo= userPositionService.getUserPosition(userID);
        ObjectRestResponse res = new ObjectRestResponse() ;
        res.rel(true);
        res.data(userInfo);
        res.msg("200");
        return  res;
    }

}
