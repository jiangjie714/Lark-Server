package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应team.js
 */
@RestController
@RequestMapping(value = "/team")
public class TeamController {


    /**
     * 项目团队
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/team', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 项目团队列表
        return new TableResultResponse();
    }

    /**
     * 查看团队
     * @param {*} teamCode
     *   export function read(teamCode) {
     *       return $http.post('project/team/read', {teamCode: teamCode});
     *   }
     */
    @RequestMapping(value = "/read",method = RequestMethod.POST)
    public ObjectRestResponse task_workflow(@RequestBody Object data){
        return new ObjectRestResponse().data("团队具体信息");
    }

    /**
     * 新增/编辑
     * @param {*} data
     *   export function doData(data) {
     *       let url = 'project/team/save';
     *       if (data.teamCode) {
     *           url = 'project/team/edit';
     *       }
     *       return $http.post(url, data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody Object data){
        return new BaseResponse(200,"新增成功！");
    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody Object data){
        return new BaseResponse(200,"修改成功！");
    }
    /**
     * 删除
     * @param {*} teamCode
     *   export function del(teamCode) {
     *       return $http.delete('project/team/delete', {teamCode: teamCode});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("teamCode") String teamCode){
        return new BaseResponse(200,"删除成功！");
    }

}
