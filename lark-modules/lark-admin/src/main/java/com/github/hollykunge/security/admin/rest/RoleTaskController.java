package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.biz.RoleTaskBiz;
import com.github.hollykunge.security.admin.entity.RoleTask;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-4-28
 * @deprecation 仅限于项目 任务 的角色操作使用
 */
@Controller
@RequestMapping("/role")
public class RoleTaskController extends BaseController<RoleTaskBiz, RoleTask> {


    /**
     * 新建一个角色
     * @param roleTask
     * @return
     */
    @RequestMapping(value = "/taskRole",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<RoleTask> addTaskRole(@RequestBody RoleTask roleTask){
        baseBiz.insertSelective(roleTask);
        return new ObjectRestResponse<>().data(roleTask).rel(true);
    }
    //update
    @RequestMapping(value = "/taskRole",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<RoleTask> updateTaskRole(@RequestBody RoleTask roleTask){
        baseBiz.updateSelectiveById(roleTask);
        return new ObjectRestResponse<>().data(roleTask).rel(true);
    }
    //role  这个在elementTask中写了
    //delete
    @RequestMapping(value = "/taskRole",method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<RoleTask> deleteTaskRole(@RequestParam("roleTaskId")String roleTaskId){
        RoleTask roleTask = baseBiz.selectById(roleTaskId);
        baseBiz.deleteById(roleTaskId);
        return new ObjectRestResponse<>().data(roleTask).rel(true);
    }
}
