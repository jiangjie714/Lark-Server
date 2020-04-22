package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.biz.ElementBiz;
import com.github.hollykunge.security.admin.entity.Element;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-23 20:30
 */
@Controller
@RequestMapping("element")
public class ElementController extends BaseController<ElementBiz, Element> {

}
