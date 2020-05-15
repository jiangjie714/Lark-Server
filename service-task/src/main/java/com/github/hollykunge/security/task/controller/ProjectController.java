package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.biz.LarkProjectBiz;
import com.github.hollykunge.security.task.biz.LarkProjectMemberBiz;
import com.github.hollykunge.security.task.biz.LarkTaskStagesBiz;
import com.github.hollykunge.security.task.dto.LarkProjectDto;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.feign.LarkProjectFileFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

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

    @Autowired
    private LarkTaskStagesBiz larkTaskStagesbiz;

    @Autowired
    private LarkProjectMemberBiz larkProjectMemberbiz;

    @Autowired
    private LarkProjectFileFeign larkProjectTemplateFeign;
    /**
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
    @RequestMapping(value = "/operation",method = RequestMethod.POST)
    public ObjectRestResponse<LarkProject> createProject(@RequestBody LarkProject project, HttpServletRequest request){
       return larkProjectBiz.createProject(project,request);
    }

    @Override
    public ObjectRestResponse<LarkProject> add(LarkProject larkProject) {
        return super.add(larkProject);
    }

    /**
     * 删除项目
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/project/delete', {projectCode: code});
     *  }
     */
    @RequestMapping(value = "/operation",method = RequestMethod.DELETE)
    public ObjectRestResponse<LarkProject> delete(@RequestParam("projectCode") String projectCode){
        if(StringUtils.isEmpty(projectCode)){
           throw  new ClientParameterInvalid("项目编码不可为空！");
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
    @RequestMapping(value = "/operation",method = RequestMethod.PUT)
    public ObjectRestResponse<LarkProject> edit(@RequestBody LarkProject project){
        if(StringUtils.isEmpty(project.getId())){
            throw new ClientParameterInvalid("更新项目id不可为空！");
        }
        larkProjectBiz.updateSelectiveById(project);
        return new ObjectRestResponse().msg("修改成功！");
    }

    /**
     * 封面存储
     * @return
     */
    @RequestMapping(value = "/editCover",method = RequestMethod.POST)
    public FileInfoVO projectTemplateCover(@RequestParam("file") MultipartFile file){
        ObjectRestResponse<FileInfoVO> objectRestResponse= larkProjectTemplateFeign.projectTemplateCover(file);
        FileInfoVO fileInfoVO = objectRestResponse.getResult();
        return fileInfoVO;
    }
    /**
     * 我的项目
     * @param {*} data
     *   export function selfList(data) {
     *       return $http.get('project/project/selfList', data);
     *   }
     */
    @RequestMapping(value = "/selfList",method = RequestMethod.GET)
    public TableResultResponse<LarkProjectDto> selfList(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        return larkProjectBiz.selectByQueryUserInfo(query);
    }


    /**
     * 查看项目
     * @param {*} code
     *   export function read(code) {
     *       return $http.get('project/project/read', {projectCode: code});
     *   }
     */
    @RequestMapping(value = "/operation",method = RequestMethod.GET)
    public ObjectRestResponse read(@RequestParam("projectCode") String projectCode){
        return larkProjectBiz.getProjectUser(projectCode);
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
            throw new ClientParameterInvalid("项目id不可为空！");
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
            throw new ClientParameterInvalid("项目id不可为空！");
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
    public ObjectRestResponse<LarkProject> archive(@RequestParam("projectCode") String projectCode){
        LarkProject larkProject = new LarkProject();
        larkProject.setArchive(1);
        larkProject.setArchiveTime(new Date());
        baseBiz.updateSelectiveById(larkProject);
        return new ObjectRestResponse<>().msg("项目已归档！").rel(true);
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
        LarkProject larkProject = new LarkProject();
        larkProject.setArchive(0);
        baseBiz.updateSelectiveById(larkProject);
        return new BaseResponse(200,"项目已还原归档！");
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


    /**
     * 根据用户id 获取所参与项目的权限资源列表
     * @param userId 用户id
     * @return list userPermission
     */
    @RequestMapping(value = "/permission",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<LarkProjectDto> getProjectResourceToUser(@RequestParam("userId")String userId){
        return larkProjectBiz.getProjectResourceToUser(userId);
    }
}

