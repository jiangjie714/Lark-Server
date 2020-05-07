package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.dto.LarkProjectDto;
import com.github.hollykunge.security.task.entity.LarkProject;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkProjectMapper extends Mapper<LarkProject> {

    /**
     *
     * @param userId
     * @return
     */
    List<LarkProjectDto> getProjectOwner(@Param("userId") String userId);

    /**
     * 根据用户id查询 project
     * @param userId
     * @return
     */
    List<LarkProject> getProjectByUserId(@Param("userId") String userId);

    /**
     * 根据用户id 获取所参与项目的权限资源列表
     * @param userId
     * @return
     */
    List<LarkProjectDto> getProjectResourceToUser(@Param("userId") String userId);
}