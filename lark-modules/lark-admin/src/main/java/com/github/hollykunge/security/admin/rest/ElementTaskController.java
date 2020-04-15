package com.github.hollykunge.security.admin.rest;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.biz.ElementTaskBiz;
import com.github.hollykunge.security.admin.entity.AdminElementTask;
import com.github.hollykunge.security.admin.vo.AdminPermission;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation task角色资源处理类
 */
@RestController
@RequestMapping
public class ElementTaskController extends BaseController<ElementTaskBiz, AdminElementTask> {

    /**
     * @since 4-15修改
     * 批量修改角色菜单
     * @param permissionMap  包含 角色id和权限list
     */
    @RequestMapping(value = "/permission", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyMenuAuthority(@RequestBody Map<String,Object> permissionMap) {
        String id =(String)permissionMap.get("id");
        List<AdminPermission> permissionList;
        JSON.toJSONString(permissionMap.get("permissionList"));
        permissionList = JSON.parseArray(JSON.toJSONString(permissionMap.get("permissionList")),AdminPermission.class);
        baseBiz.modifyAuthorityMenu(id, permissionList);
        return new ObjectRestResponse().rel(true);
    }

    /**
     * @since 4-15修改
     * 获取菜单和关联功能
     * @param id 角色id
     */
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String,Object>> getMenuAuthority(@RequestParam("id") String id) {
        Map<String,Object> authorityMenu = baseBiz.getAuthorityMenu(id);
        return new ObjectRestResponse().data(authorityMenu);
    }

    /**
     * @since 4-15修改
     * 获取菜单和关联功能
     * @param id 角色id
     */
    @RequestMapping(value = "/rolePermission", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<AdminElementTask>> getRoleMenuAuthority(@RequestParam("id") String id) {
        List<AdminElementTask> authorityMenu = baseBiz.getAuthorityMenuByRoleId(id);
        return new ObjectRestResponse().data(authorityMenu);
    }
}
