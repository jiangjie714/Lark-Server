package com.github.hollykunge.security.admin.rest;

import com.alibaba.fastjson.JSON;
import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.biz.RoleBiz;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Role;
import com.github.hollykunge.security.admin.vo.AdminPermission;
import com.github.hollykunge.security.admin.vo.RoleTree;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.TreeUtil;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 协同设计小组
 * @create 2017-06-12 8:49
 */

@Controller
@RequestMapping("role")
@Api("角色模块")
public class RoleController extends BaseController<RoleBiz, Role> {

    /**
     * todo:使用
     * 根据角色获取用户
     *
     * @param id 角色id
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> getUsers(@RequestParam("id") String id) {
        List<AdminUser> roleUsers = baseBiz.getRoleUsers(id);
        return new ListRestResponse("",roleUsers.size(),roleUsers);
    }

    /**
     * todo:使用
     * 批量修改角色用户
     */
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@RequestBody Map<String,Object> map) {
        String id =  (String)map.get("id");
        String users =  (String)map.get("users");
        baseBiz.modifyRoleUsers(id, users);
        return new ObjectRestResponse().rel(true).msg("修改成功");
    }

    /**
     * todo:使用
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
     * todo:使用
     * 获取菜单和关联功能
     *
     * @param id 角色id
     */
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String,Object>> getMenuAuthority(@RequestParam("id") String id) {
        Map<String,Object> authorityMenu = baseBiz.getAuthorityMenu(id);
        return new ObjectRestResponse().data(authorityMenu);
    }


    /**
     * 获取角色树
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<RoleTree>> tree(@RequestParam("parentTreeId") String parentTreeId) {
        if(StringUtils.isEmpty(parentTreeId)){
            parentTreeId = AdminCommonConstant.ROOT;
        }
        List<RoleTree> tree = getTree(baseBiz.selectListAll(), parentTreeId);
        return new ListRestResponse("",tree.size(),tree);
    }

    private List<RoleTree> getTree(List<Role> roles, String root) {
        List<RoleTree> trees = new ArrayList<>();
        for (Role role : roles) {
            RoleTree node = new RoleTree();
            node.setLabel(role.getName());
            String jsonNode = JSON.toJSONString(role);
            node = JSON.parseObject(jsonNode,RoleTree.class);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }
}
