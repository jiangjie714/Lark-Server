package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.biz.LarkProjectTemplatebiz;
import com.github.hollykunge.security.task.entity.LarkProjectTemplate;
import com.github.hollykunge.security.task.feign.LarkProjectTemplateFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectTemplate.js
 */
@RestController
@RequestMapping(value = "/project_template")
public class ProjectTemplateController extends BaseController<LarkProjectTemplatebiz, LarkProjectTemplate> {

    @Autowired
    private LarkProjectTemplatebiz larkProjectTemplatebiz;

    /**
     * todo BaseController 分页
     * 项目模版列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_template', data);
     *   }
     */


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
    public ObjectRestResponse<LarkProjectTemplate> save(@RequestBody LarkProjectTemplate larkProjectTemplate,@RequestParam("file") MultipartFile file){
        LarkProjectTemplate projectTemplate = new LarkProjectTemplate();
        if(file!=null){
            FileInfoVO fileInfoVO = larkProjectTemplatebiz.projectTemplateCover(file);
            projectTemplate.setCover(fileInfoVO.getFullPath());
        }
        projectTemplate.setId(UUIDUtils.generateShortUuid());
        baseBiz.insertSelective(projectTemplate);
        return new ObjectRestResponse<LarkProjectTemplate>().data(projectTemplate).rel(true).msg("模板创建成功");
    }

    @RequestMapping(value = "/edit",method = RequestMethod.PUT)
    public ObjectRestResponse<LarkProjectTemplate> edit(@RequestBody LarkProjectTemplate larkProjectTemplate,@RequestParam("file") MultipartFile file){
        if(file!=null){
            FileInfoVO fileInfoVO  = larkProjectTemplatebiz.projectTemplateCover(file);
            larkProjectTemplate.setCover(fileInfoVO.getFullPath());
            baseBiz.updateSelectiveById(larkProjectTemplate);
        }
        baseBiz.updateSelectiveById(larkProjectTemplate);
        return new ObjectRestResponse<LarkProjectTemplate>().data(larkProjectTemplate).rel(true).msg("模板修改成功");
    }
    /**
     * todo BaseController
     * 删除模版
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/project_template/delete', {code: code});
     *   }
     */


}
