package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectTemplate.js
 */
@RestController
@RequestMapping(value = "/project_template")
public class ProjectTemplateController {

    /**
     * 项目模版列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_template', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 项目模板列表
        return new TableResultResponse();
    }
    /**
     * 保存/编辑模版
     * @param {*} data
     *   export function doData(data) {
     *       let url = 'project/project_template/save';
     *       if (data.code) {
     *           url = 'project/project_template/edit';
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
     * 删除模版
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/project_template/delete', {code: code});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResponse delete(@RequestParam("code") String code){
        return new BaseResponse(200,"模板已移除！");
    }
}
