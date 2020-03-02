package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectFeatures.js
 */
@RestController
@RequestMapping(value = "/project_features")
public class ProjectFeaturesController {


    /**
     * 项目版本库列表
     * @param {} data
     *   export function list(data) {
     *       return $http.get('project/project_features', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空
        return new TableResultResponse();
    }
    /**
     * 版本库保存
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/project_features/save', data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody Object data){
        return new BaseResponse(200,"版本库保存成功！");
    }
    /**
     * 版本库编辑
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/project_features/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ObjectRestResponse edit(@RequestBody Object data){
        return new ObjectRestResponse();
    }
    /**
     * 版本库删除
     * @param {*} data
     *   export function del(data) {
     *       return $http.delete('project/project_features/delete', data);
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody Object data){
        return new BaseResponse(200,"版本库已删除！");
    }
}
