package com.github.hollykunge.security.admin.rest;

import com.alibaba.fastjson.JSON;

import com.github.hollykunge.security.admin.biz.ElementBiz;
import com.github.hollykunge.security.admin.biz.MenuBiz;
import com.github.hollykunge.security.admin.entity.Menu;
import com.github.hollykunge.security.admin.vo.AdminElement;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-12 8:49
 */
@Controller
@RequestMapping("menu")
public class MenuController extends BaseController<MenuBiz, Menu> {

    @Autowired
    private ElementBiz elementBiz;

    /**
     * todo:使用
     * @param menu
     * @return
     */
    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Menu> add(@RequestBody Menu menu) {
        baseBiz.insertSelective(menu);
        return new ObjectRestResponse<>().data(menu);
    }

    /**
     * todo:使用
     * 根据menuId获取element
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/element",method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminElement>> listMenuElement(@RequestParam("menuId") String menuId){
        List<AdminElement> adminElements = elementBiz.listMenuElement(menuId);
        return new ListRestResponse("",adminElements.size(),adminElements);
    }

    /**
     * todo:使用
     * 给menu添加element或修改element
     * @return
     */
    @RequestMapping(value = "/element",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyMenuElement(@RequestBody Map<String,Object> map){
        String menuId = (String) map.get("menuId");
        List<AdminElement> elementList = JSON.parseArray(JSON.toJSONString(map.get("elements")),AdminElement.class) ;
        elementBiz.modifyMenuElement(menuId,elementList);
        return new ObjectRestResponse().rel(true);
    }
}
