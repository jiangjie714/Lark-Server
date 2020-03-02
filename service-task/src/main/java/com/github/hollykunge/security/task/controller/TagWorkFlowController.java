package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskWorkFlow.js
 */
@RestController
@RequestMapping(value = "/task_workflow")
public class TagWorkFlowController {


    /**
     * 任务工作流列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_workflow', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse task_workflow(@RequestBody Object data){
        // todo 暂时返回空 任务工作流列表
        return new TableResultResponse();
    }
    /**
     * 新增
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/task_workflow/save', data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody Object data){
        return new BaseResponse(200,"新增成功！");
    }

    /**
     * 编辑
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/task_workflow/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody Object data){
        return new BaseResponse(200,"修改成功！");
    }

    /**
     * 删除
     * @param {*} data
     *   export function del(data) {
     *       return $http.delete('project/task_workflow/delete', data);
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody Object data){
        return new BaseResponse(200,"删除成功！");
    }

}
