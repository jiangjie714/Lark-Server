package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkProjectBiz;
import com.github.hollykunge.security.task.entity.LarkProject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应project.js
 */
@RestController
@RequestMapping(value = "/project")
public class ProjectController extends BaseController<LarkProjectBiz, LarkProject> {

    @Autowired
    private LarkProjectBiz larkProjectBiz;

    /**
     * fansq
     * 创建项目
     * @param project
     *   export function doData(data) {
     *      let url = 'project/project/save';
     *      if (data.projectCode) {
     *         url = 'project/project/edit';
     *      }
     *      return $http.post(url, data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ObjectRestResponse<LarkProject> createProject(@RequestBody LarkProject project){
        project.setId(UUIDUtils.generateShortUuid());
        larkProjectBiz.insertSelective(project);
        return new ObjectRestResponse<>().data(project).msg("项目新建成功！").rel(true);
    }

    /**
     * 删除项目
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/project/delete', {projectCode: code});
     *  }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ObjectRestResponse<LarkProject> delete(@RequestParam("projectCode") String projectCode){
        if(StringUtils.isEmpty(projectCode)){
           throw  new BaseException("项目编码不可为空！");
        }
        return larkProjectBiz.deleteProject(projectCode);
    }

    /**
     * 编辑
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
    public ObjectRestResponse<LarkProject> edit(@RequestBody LarkProject project){
        if(StringUtils.isEmpty(project.getId())){
            throw new BaseException("更新项目id不可为空！");
        }
        larkProjectBiz.updateSelectiveById(project);
        return new ObjectRestResponse().msg("修改成功！");
    }

    /**
     * 我的项目
     * @param {*} data
     *   export function selfList(data) {
     *       return $http.get('project/project/selfList', data);
     *   }
     * todo 接口重复 项目列表
     */
    @RequestMapping(value = "/selfList",method = RequestMethod.GET)
    public TableResultResponse<Object> selfList(@RequestBody Object data){
        return new TableResultResponse<>();
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
        if(StringUtils.isEmpty(projectCode)){
            throw new BaseException("项目id不可为空！");
        }
        LarkProject larkProject = new LarkProject();
        larkProject.setId(projectCode);
        larkProject.setDeleted(1);
        larkProject.setDeletedTime(new Date());
        larkProjectBiz.updateSelectiveById(larkProject);
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
        if(StringUtils.isEmpty(projectCode)){
            throw new BaseException("项目id不可为空！");
        }
        LarkProject larkProject = new LarkProject();
        larkProject.setId(projectCode);
        larkProject.setDeleted(0);
        larkProject.setDeletedTime(new Date());
        larkProjectBiz.updateSelectiveById(larkProject);
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
     * 查看项目
     * @param {*} code
     *   export function read(code) {
     *       return $http.get('project/project/read', {projectCode: code});
     *   }
     *   todo 暂未完成
     */
    @RequestMapping(value = "/read",method = RequestMethod.GET)
    public ObjectRestResponse read(@RequestParam("projectCode") String projectCode){
        LarkProject larkProject = baseBiz.selectById(projectCode);
        return new ObjectRestResponse().msg("查询成功！").data(larkProject).rel(true);
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

