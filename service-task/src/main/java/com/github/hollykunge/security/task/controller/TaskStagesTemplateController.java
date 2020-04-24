package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.biz.LarkTaskStagesTemplatebiz;
import com.github.hollykunge.security.task.entity.LarkTaskStagesTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskStagesTemplate.js
 */
@RestController
@RequestMapping(value = "/task_stages_template")
public class TaskStagesTemplateController extends BaseController<LarkTaskStagesTemplatebiz, LarkTaskStagesTemplate> {

    @Autowired
    private LarkTaskStagesTemplatebiz larkTaskStagesTemplatebiz;
    /**
     * 任务阶段模版列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_stages_template', data);
     *   }
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public TableResultResponse<List<LarkTaskStagesTemplate>> list(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        larkTaskStagesTemplatebiz.list(query);
        return new TableResultResponse();
    }

    /**
     * 编辑/新增
     * @param {*} data
     *   export function doData(data) {
     *       let url = 'project/task_stages_template/save';
     *       if (data.code) {
     *           url = 'project/task_stages_template/edit';
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
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/task_stages_template/delete', {code: code});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody Object data){
        return new BaseResponse(200,"删除成功！");
    }


}
