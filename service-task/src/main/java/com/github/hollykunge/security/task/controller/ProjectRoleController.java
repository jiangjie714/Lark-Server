package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.task.entity.ProjectRole;
import com.github.hollykunge.security.task.service.ProjectRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 角色操作controller
 */
@RestController
@RequestMapping(value = "/role")
public class ProjectRoleController {

    @Autowired
    private ProjectRoleService projectRoleService;

    /**
     * 新建一个角色并分配权限
     * @param projectRole
     * @return
     * 成功示例
     * {
     * 	"_id" : ObjectId("5e624f5e43dbdf352041eb91"),
     * 	"role_id" : "1",
     * 	"title" : "fansq测试",
     * 	"element_list" : [ 权限信息
     *       {"element_id" : "29"},{"element_id" : "30"},{"element_id" : "31"},{"element_id" : "32"},
     *       {"element_id" : "33"},{"element_id" : "34"},{"element_id" : "35"},{"element_id" : "36"}
     * 	],
     * 	"status" : "0",
     * 	"smallint" : 1,
     * 	"desc" : "测试用户请勿删除",
     * 	"organization_code" : "00100014",
     * 	"team_code" : "云雀协同",
     * 	"is_default" : "1",
     * 	"_class" : "com.github.hollykunge.security.task.entity.ProjectRole"
     * }
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ObjectRestResponse<ProjectRole> addRole(@RequestBody ProjectRole projectRole){
        projectRoleService.addRole(projectRole);
        return new ObjectRestResponse<>().data(projectRole).msg("新增角色成功！");
    }

    /**
     * fansq
     * 分页查询角色信息
     * @param pageNum 页数
     * @param pageSize 条数
     * @return
     */
    @RequestMapping(value = "/findRole",method = RequestMethod.GET)
    public TableResultResponse<ProjectRole> findRole(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize){
        return projectRoleService.findRole(pageNum,pageSize);
    }
}
