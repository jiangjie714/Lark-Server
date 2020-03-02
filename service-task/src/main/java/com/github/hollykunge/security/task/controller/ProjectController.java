package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应project.js
 */
@RestController
@RequestMapping(value = "/project")
public class ProjectController {


    /**
     * 项目列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse<Object> list(@RequestBody Object data){
        return new TableResultResponse<>();
    }

    /**
     * 我的项目
     * @param {*} data
     *   export function selfList(data) {
     *       return $http.get('project/project/selfList', data);
     *   }
     */
    @RequestMapping(value = "/selfList",method = RequestMethod.GET)
    public TableResultResponse<Object> selfList(@RequestBody Object data){
        return new TableResultResponse<>();
    }

    /**
     * 保存/编辑
     * @param {*} data
     *   export function doData(data) {
     *       let url = 'project/project/save';
     *       if (data.projectCode) {
     *           url = 'project/project/edit';
     *       }
     *       return $http.post(url, data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ObjectRestResponse save(@RequestBody Object data){
        return new ObjectRestResponse().msg("保存成功！");
    }

    /**
     * 保存/编辑
     * @param {*} data
     *   export function doData(data) {
     *       let url = 'project/project/save';
     *       if (data.projectCode) {
     *           url = 'project/project/edit';
     *       }
     *       return $http.post(url, data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ObjectRestResponse edit(@RequestBody Object data){
        return new ObjectRestResponse().msg("修改成功！");
    }

    /**
     * 退出项目
     * @param {} code
     *    export function quit(code) {
     *       return $http.post('project/project/quit', {projectCode: code});
     *   }
     */
    @RequestMapping(value = "/quit",method = RequestMethod.POST)
    public BaseResponse quit(@RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"项目已退出！");
    }

    /**
     * 回收项目
     * @param {*} code
     *   export function recycle(code) {
     *       return $http.post('project/project/recycle', {projectCode: code});
     *   }
     */
    @RequestMapping(value = "/recycle",method = RequestMethod.POST)
    public BaseResponse recycle(@RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"项目已回收！");
    }

    /**
     * 还原项目
     * @param {*} code
     *   export function recovery(code) {
     *       return $http.post('project/project/recovery', {projectCode: code});
     *   }
     */
    @RequestMapping(value = "/recovery",method = RequestMethod.POST)
    public BaseResponse recovery(@RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"项目已还原！");
    }

    /**
     * 归档项目
     * @param {*} code
     *
     *   export function archive(code) {
     *       return $http.post('project/project/archive', {projectCode: code});
     *   }
     */
    @RequestMapping(value = "/archive",method = RequestMethod.POST)
    public BaseResponse archive(@RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"项目已归档！");
    }

    /**
     * 还原归档
     * @param {*} code
     *export function recoveryArchive(code) {
     *    return $http.post('project/project/recoveryArchive', {projectCode: code});
     * }
     */
    @RequestMapping(value = "/recoveryArchive",method = RequestMethod.POST)
    public BaseResponse recoveryArchive(@RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"项目已还原归档！");
    }

    /**
     * 删除项目
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/project/delete', {projectCode: code});
     *  }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("projectCode") String projectCode){
        return new BaseResponse(200,"项目已删除！");
    }

    /**
     * 查看项目
     * @param {*} code
     *   export function read(code) {
     *       return $http.get('project/project/read', {projectCode: code});
     *   }
     */
    @RequestMapping(value = "/read",method = RequestMethod.GET)
    public ObjectRestResponse read(@RequestParam("projectCode") String projectCode){
        return new ObjectRestResponse().msg("").data("项目具体内容");
    }

    /**
     * 项目统计
     * @param {*} data
     *   export function analysis(data) {
     *       return $http.get('project/project/analysis', data);
     *   }
     */
    @RequestMapping(value = "/analysis",method = RequestMethod.GET)
    public ObjectRestResponse analysis(@RequestBody Object data){
        return new ObjectRestResponse();
    }


}

