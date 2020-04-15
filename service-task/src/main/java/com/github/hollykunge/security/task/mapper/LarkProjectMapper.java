package com.github.hollykunge.security.task.mapper;

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
     * 根据用户id查询 project
     * @param userId
     * @return
     */
    public List<LarkProject> getProjectPageListByUserId(@Param("userId") String userId);

}