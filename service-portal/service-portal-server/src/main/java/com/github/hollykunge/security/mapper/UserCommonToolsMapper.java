package com.github.hollykunge.security.mapper;

import com.github.hollykunge.security.entity.UserCommonTools;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 用户设置常用工具实体类
 * @author zhhongyu
 */
public interface UserCommonToolsMapper extends Mapper<UserCommonTools> {

    /**
     * fansq
     * 插入接口
     * @param userCommonTools
     * @return
     */
    int insertCommonTools(@Param("userCommonTools") List<UserCommonTools> userCommonTools);

    /**
     * fansq
     * 删除接口
     * @param toolId
     * @param userCommonToolsList
     */
    void deleteUserCommonTools(@Param("toolId") String toolId,
                               @Param("userCommonToolsList") List<UserCommonTools> userCommonToolsList);
    /**
     * fansq
     * 19-12-12
     * 查询接口
     */
    List<UserCommonTools> selectUserCommonTools(@Param("toolId") String toolId,
                                                @Param("userCommonToolsList") List<UserCommonTools> userCommonToolsList);
}
