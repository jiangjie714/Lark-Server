package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.task.entity.ProjectElement;
import com.github.hollykunge.security.task.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fansq all
 * @since 20-3-6
 * @deprecation 资源列表处理
 */
@RestController
@RequestMapping(value = "/element")
public class ElementController {

    @Autowired
    private ElementService elementService;

    /**
     * 20-2-3
     * @return 资源列表
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public List<ProjectElement> findAll(){
        return elementService.findAllElemet();
    }
}
