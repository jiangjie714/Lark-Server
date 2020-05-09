package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.dto.LarkProjectTemplateDto;
import com.github.hollykunge.security.task.entity.LarkProjectTemplate;
import com.github.hollykunge.security.task.feign.LarkProjectFileFeign;
import com.github.hollykunge.security.task.mapper.LarkProjectTemplateMapper;
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
public class LarkProjectTemplatebiz extends BaseBiz<LarkProjectTemplateMapper, LarkProjectTemplate> {

    @Autowired
    private LarkProjectTemplateMapper larkProjectTemplateMapper;

    @Autowired
    private LarkProjectFileFeign larkProjectTemplateFeign;

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
}
