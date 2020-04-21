package com.github.hollykunge.security.task.biz;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.entity.LarkProjectTemplate;
import com.github.hollykunge.security.task.feign.LarkProjectTemplateFeign;
import com.github.hollykunge.security.task.mapper.LarkProjectTemplateMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private LarkProjectTemplateFeign larkProjectTemplateFeign;

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
    @Override
    public TableResultResponse<LarkProjectTemplate> selectByQuery(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LarkProjectTemplate> larkProjectTemplates = larkProjectTemplateMapper.selectAll();
        return new TableResultResponse<LarkProjectTemplate>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkProjectTemplates);
    }

    @Override
    protected String getPageName() {
        return null;
    }
}
