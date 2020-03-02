package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskTag.js
 */
@RestController
@RequestMapping(value = "/task_tag")
public class TaskTagController {


    /**
     * 任务标签
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_tag', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 任务标签 列表
        return new TableResultResponse();
    }

    /**
     * 新增
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/task_tag/save', data);
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
     *       return $http.post('project/task_tag/edit', data);
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
     *       return $http.delete('project/task_tag/delete', {tagCode: code});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("tagCode") String tagCode){
        return new BaseResponse(200,"删除成功！");
    }
}
