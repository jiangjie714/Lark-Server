package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.dto.LarkProjectTemplateDto;
import com.github.hollykunge.security.task.entity.LarkProjectTemplate;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkProjectTemplateMapper extends Mapper<LarkProjectTemplate> {

    /**
     * 获取项目模板信息
     * 不考虑部门
     * @return
     */
    List<LarkProjectTemplateDto> getProjectTemplateList();
}