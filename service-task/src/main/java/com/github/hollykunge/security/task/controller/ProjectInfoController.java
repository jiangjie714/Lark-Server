package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectInfo.js
 */
@RestController
@RequestMapping(value = "/project_info")
public class ProjectInfoController {

    /**
     * 项目信息
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_info', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ObjectRestResponse list(@RequestBody Object data){
        return new ObjectRestResponse();
    }
    /**
     * 保存/编辑项目信息
     * @param {*} data
     *   export function doData(data) {
     *       let url = 'project/project_info/save';
     *       if (data.infoCode) {
     *           url = 'project/project_info/edit';
     *       }
     *       return $http.post(url, data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody Object data){
        return new BaseResponse(200,"保存成功！");
    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody Object data){
        return new BaseResponse(200,"修改成功！");
    }
    /**
     * 删除项目信息
     * @param {*} data
     *   export function del(data) {
     *       return $http.delete('project/project_info/delete', data);
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody Object data){
        return new BaseResponse(200,"项目信息已删除！");
    }
}
