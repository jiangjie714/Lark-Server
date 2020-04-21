package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkTaskStagesbiz;
import com.github.hollykunge.security.task.dto.LarkTaskDto;
import com.github.hollykunge.security.task.dto.LarkTaskStagesDto;
import com.github.hollykunge.security.task.entity.LarkTaskStages;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-3-2  20-4-17修改
 * @deprecation 对应taskStages.js
 */
@RestController
@RequestMapping(value = "/task_stages")
public class TaskStagesController extends BaseController<LarkTaskStagesbiz, LarkTaskStages> {

    @Autowired
    private LarkTaskStagesbiz larkTaskStagesbiz;
    /**
     * 任务阶段列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_stages', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 任务阶段列表  getTasksByProjectId
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
    @RequestMapping(value = "/sort",method = RequestMethod.POST)
    public BaseResponse sort(@RequestBody List<LarkTaskStages> larkTaskStages){
        for(LarkTaskStages larkTaskStage:larkTaskStages){
            baseBiz.updateSelectiveById(larkTaskStage);
        }
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
    public ObjectRestResponse<LarkTaskStages> save(@RequestBody LarkTaskStages larkTaskStages){
        larkTaskStages.setId(UUIDUtils.generateShortUuid());
        baseBiz.insertSelective(larkTaskStages);
        return new ObjectRestResponse<>().data(larkTaskStages).rel(true).msg("新增成功任务列表");
    }


    /**
     * 编辑阶段
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/task_stages/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody LarkTaskStages larkTaskStages){
        baseBiz.updateSelectiveById(larkTaskStages);
        return new BaseResponse(200,"修改成功！");
    }


    /**
     * 删除
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/task_stages/delete', {code: code});
     *   }
     *   更新deleted 字段
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("stagesId") String stagesId){
        LarkTaskStages larkTaskStages = new LarkTaskStages();
        larkTaskStages.setDeleted(1);
        larkTaskStages.setId(stagesId);
        baseBiz.updateSelectiveById(larkTaskStages);
        return new BaseResponse(200,"删除成功！");
    }


    /**
     * 首页点击项目 进入具体项目看到的页面数据
     * 获取指定项目的任务列表
     * @param projectCode
     * @return
     */
    @RequestMapping(value = "/getTasksByProjectId",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<LarkTaskStagesDto>> getTasksByProjectId(@RequestParam("projectCode") String projectCode){
        List<LarkTaskStagesDto>  larkTaskStagesDtos= larkTaskStagesbiz.getTasksByProjectId(projectCode);
        return new ObjectRestResponse<>().data(larkTaskStagesDtos).rel(true);
    }


    /**
     * 拆分请求第一步 getTasksByProjectId  先获取任务列 列表
     * 根据项目id 获取任务列 列表
     * @return
     */
    @RequestMapping(value = "/getStagesList",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<LarkTaskStages>> getStagesList(@RequestParam("projectCode") String projectCode){
        return larkTaskStagesbiz.getStagesList(projectCode);
    }

}
