package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectVersion.js
 */
@RestController
@RequestMapping(value = "/project_version")
public class ProjectVersionController {

    /**
     * 项目版本列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_version', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 项目版本列表
        return new TableResultResponse();
    }

    /**
     * 获取项目版本下的任务
     * @param {*} data
     *   export function getVersionTask(data) {
     *       return $http.get('project/project_version/_getVersionTask', data);
     *   }
     */
    @RequestMapping(value = "/getVersionTask",method = RequestMethod.GET)
    public TableResultResponse getVersionTask(@RequestBody Object data){
        // todo 暂时返回空 项目版本下的任务列表
        return new TableResultResponse();
    }

    /**
     * 获取项目版本日志
     * @param {*} data
     *   export function getVersionLog(data) {
     *       return $http.get('project/project_version/_getVersionLog', data);
     *   }
     */
    @RequestMapping(value = "/getVersionLog",method = RequestMethod.GET)
    public TableResultResponse getVersionLog(@RequestBody Object data){
        // todo 暂时返回空 项目版本日志列表
        return new TableResultResponse();
    }

    /**
     * 查看版本信息
     * @param {*} data
     *   export function read(data) {
     *       return $http.get('project/project_version/read', data);
     *   }
     */
    @RequestMapping(value = "/read",method = RequestMethod.GET)
    public ObjectRestResponse read(@RequestBody Object data){
        return new ObjectRestResponse().data("版本信息具体内容").msg("查询成功！");
    }

    /**
     * 新增版本信息
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/project_version/save', data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody Object data){
        return new BaseResponse(200,"新增成功！");
    }

    /**
     * 编辑项目版本信息
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/project_version/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody Object data){
        return new BaseResponse(200,"项目版本信息已修改！");
    }

    /**
     * 添加项目版本任务
     * @param {*} data
     *   export function addVersionTask(data) {
     *       return $http.post('project/project_version/addVersionTask', data);
     *   }
     */
    @RequestMapping(value = "/addVersionTask",method = RequestMethod.POST)
    public ObjectRestResponse addVersionTask(@RequestBody Object data){
        return new ObjectRestResponse().data("返回新增项目版本任务具体内容").msg("版本任务新增成功！");
    }
    /**
     * 删除项目版本任务
     * @param {*} data
     *   export function removeVersionTask(data) {
     *       return $http.delete('project/project_version/removeVersionTask', data);
     *   }
     */
    @RequestMapping(value = "/removeVersionTask",method = RequestMethod.DELETE)
    public BaseResponse removeVersionTask(@RequestBody Object data){
        return new BaseResponse(200,"项目版本任务已删除！");
    }
    /**
     * 修改项目版本状态
     * @param {*} data
     *   export function changeStatus(data) {
     *       return $http.post('project/project_version/changeStatus', data);
     *   }
     */
    @RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
    public BaseResponse changeStatus(@RequestBody Object data){
        return new BaseResponse(200,"项目版本状态已修改！");
    }
    /**
     * 删除版本
     * @param {*} data
     *   export function del(data) {
     *       return $http.delete('project/project_version/delete', data);
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody Object data){
        return new BaseResponse(200,"项目版本已删除！");
    }
}
