package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.biz.LarkProjectTemplateBiz;
import com.github.hollykunge.security.task.dto.LarkProjectTemplateDto;
import com.github.hollykunge.security.task.entity.LarkProjectTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectTemplate.js
 */
@RestController
@RequestMapping(value = "/project_template")
public class ProjectTemplateController extends BaseController<LarkProjectTemplateBiz, LarkProjectTemplate> {

    @Autowired
    private LarkProjectTemplateBiz larkProjectTemplatebiz;

    /**
     * 项目模版列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_template', data);
     *   }
     */
    @RequestMapping(value = "/getProjectTemplateList",method = RequestMethod.GET)
    public TableResultResponse<LarkProjectTemplateDto> getProjectTemplateList(@RequestParam Map<String,Object> params) {
        Query query = new Query(params);
        return larkProjectTemplatebiz.getProjectTemplateList(query);
    }

    /**
     * 保存/
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
    public ObjectRestResponse<LarkProjectTemplate> save(
            @RequestParam("projectTemplateName") String projectTemplateName,
            @RequestParam(value = "projectTemplateDescription",required=false) String projectTemplateDescription,
            @RequestParam("file") MultipartFile file){
        LarkProjectTemplate projectTemplate = new LarkProjectTemplate();
        if(file!=null){
            FileInfoVO fileInfoVO = larkProjectTemplatebiz.projectTemplateCover(file);
            projectTemplate.setCover(fileInfoVO.getFullPath());
        }
        projectTemplate.setName(projectTemplateName);
        projectTemplate.setDescription(projectTemplateDescription);
        baseBiz.insertSelective(projectTemplate);
        larkProjectTemplatebiz.save(projectTemplate);
        return new ObjectRestResponse<LarkProjectTemplate>().data(projectTemplate).rel(true).msg("模板创建成功");
    }

    @RequestMapping(value = "/edit",method = RequestMethod.PUT)
    public ObjectRestResponse<LarkProjectTemplate> edit(
            @RequestParam(value = "projectTemplateName",required=false) String projectTemplateName,
            @RequestParam("projectTemplateId") String projectTemplateId,
            @RequestParam(value = "projectTemplateDescription",required=false) String projectTemplateDescription,
            @RequestParam(value = "file",required=false) MultipartFile file){
        LarkProjectTemplate projectTemplate = new LarkProjectTemplate();
        projectTemplate.setId(projectTemplateId);
        if(!StringUtils.isEmpty(projectTemplateName)){
            projectTemplate.setName(projectTemplateName);
        }
        if(!StringUtils.isEmpty(projectTemplateDescription)){
            projectTemplate.setDescription(projectTemplateDescription);
        }
        if(file!=null){
            FileInfoVO fileInfoVO  = larkProjectTemplatebiz.projectTemplateCover(file);
            projectTemplate.setCover(fileInfoVO.getFullPath());
            baseBiz.updateSelectiveById(projectTemplate);
        }
        baseBiz.updateSelectiveById(projectTemplate);
        return new ObjectRestResponse<LarkProjectTemplate>().data(projectTemplate).rel(true).msg("模板修改成功");
    }
    /**
     * todo BaseController
     * 删除模版
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/project_template/delete', {code: code});
     *   }
     */
    @Override
    public ObjectRestResponse<LarkProjectTemplate> remove(String id) {

        return super.remove(id);
    }
}
