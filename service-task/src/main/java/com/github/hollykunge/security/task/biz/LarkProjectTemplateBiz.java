package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.dto.LarkProjectTemplateDto;
import com.github.hollykunge.security.task.entity.LarkProjectTemplate;
import com.github.hollykunge.security.task.entity.LarkTaskStagesTemplate;
import com.github.hollykunge.security.task.feign.LarkProjectFileFeign;
import com.github.hollykunge.security.task.mapper.LarkProjectTemplateMapper;
import com.github.hollykunge.security.task.mapper.LarkTaskStagesTemplateMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-15
 * @deprecation 项目模板
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LarkProjectTemplateBiz extends BaseBiz<LarkProjectTemplateMapper, LarkProjectTemplate> {

    @Autowired
    private LarkProjectTemplateMapper larkProjectTemplateMapper;

    @Autowired
    private LarkProjectFileFeign larkProjectTemplateFeign;

    @Autowired
    private LarkTaskStagesTemplateMapper larkTaskStagesTemplateMapper;
    /**
     * 封面存储
     * @return
     */
    public FileInfoVO  projectTemplateCover(MultipartFile file){
        ObjectRestResponse<FileInfoVO> objectRestResponse= larkProjectTemplateFeign.projectTemplateCover(file);
        FileInfoVO fileInfoVO = objectRestResponse.getResult();
        return fileInfoVO;
    }
    /**
     * 项目模板分页
     * @param query
     * @return
     */

    public TableResultResponse<LarkProjectTemplateDto> getProjectTemplateList(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LarkProjectTemplateDto> larkProjectTemplates = larkProjectTemplateMapper.getProjectTemplateList();
        //获取模板列表 同时 获取模板 任务列表
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkProjectTemplates);
    }

    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 新建模板默认新增3个任务列模板
     * @param projectTemplate
     */
    public void save(LarkProjectTemplate projectTemplate) {
        //待处理
        LarkTaskStagesTemplate larkTaskStages = new LarkTaskStagesTemplate();
        larkTaskStages.setName("待处理");
        larkTaskStages.setProjectTemplateCode(projectTemplate.getId());
        larkTaskStages.setDeleted("1");
        larkTaskStages.setSort(1);
        larkTaskStages.setDescription("默认任务列表");
        EntityUtils.setCreatAndUpdatInfo(larkTaskStages);
        larkTaskStagesTemplateMapper.insertSelective(larkTaskStages);
        //进行中
        LarkTaskStagesTemplate larkTask = new LarkTaskStagesTemplate();
        larkTask.setName("进行中");
        larkTask.setProjectTemplateCode(projectTemplate.getId());
        larkTask.setDeleted("1");
        larkTask.setSort(2);
        larkTask.setDescription("默认任务列表");
        EntityUtils.setCreatAndUpdatInfo(larkTask);
        larkTaskStagesTemplateMapper.insertSelective(larkTask);
        //已完成
        LarkTaskStagesTemplate TaskStages = new LarkTaskStagesTemplate();
        TaskStages.setName("已完成");
        TaskStages.setProjectTemplateCode(projectTemplate.getId());
        TaskStages.setDeleted("1");
        TaskStages.setSort(3);
        TaskStages.setDescription("默认任务列表");
        EntityUtils.setCreatAndUpdatInfo(TaskStages);
        larkTaskStagesTemplateMapper.insertSelective(TaskStages);
    }
}
