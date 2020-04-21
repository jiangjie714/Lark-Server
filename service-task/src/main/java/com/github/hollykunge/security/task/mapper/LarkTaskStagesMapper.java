package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.dto.LarkTaskStagesDto;
import com.github.hollykunge.security.task.entity.LarkTaskStages;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkTaskStagesMapper extends Mapper<LarkTaskStages> {

    List<LarkTaskStagesDto> getTasksByProjectId(@Param("projectCode") String projectCode);
}