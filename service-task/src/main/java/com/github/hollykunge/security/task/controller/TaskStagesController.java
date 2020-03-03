package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskStages.js
 */
@RestController
@RequestMapping(value = "/task_stages")
public class TaskStagesController {

    /**
     * 任务阶段列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_stages', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 任务阶段列表
        return new TableResultResponse();
    }

    /**
     * 获取当前阶段下的所有任务
     * @param {*} data
     *   export function tasks(data) {
     *       return $http.get('project/task_stages/tasks', data);
     *   }
     */
    @RequestMapping(value = "/tasks",method = RequestMethod.GET)
    public TableResultResponse tasks(@RequestBody Object data){
        // todo 暂时返回空 任务列表
        return new TableResultResponse();
    }

    /**
     * 排序
     * @param {*} preCode
     * @param {*} nextCode
     * @param {*} projectCode
     *   export function sort(preCode, nextCode, projectCode) {
     *       return $http.post('project/task_stages/sort', {preCode: preCode, nextCode: nextCode, projectCode: projectCode});
     *   }
     */
    @RequestMapping(value = "/inviteMemberBatch",method = RequestMethod.POST)
    public BaseResponse inviteMemberBatch(
            @RequestParam("preCode") String preCode,
            @RequestParam("nextCode") String nextCode,
            @RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"排序成功！");
    }


    /**
     * 新增阶段
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/task_stages/save', data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody Object data){
        return new BaseResponse(200,"新增成功！");
    }


    /**
     * 编辑阶段
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/task_stages/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody Object data){
        return new BaseResponse(200,"修改成功！");
    }


    /**
     * 删除
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/task_stages/delete', {code: code});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody Object data){
        return new BaseResponse(200,"删除成功！");
    }


}
