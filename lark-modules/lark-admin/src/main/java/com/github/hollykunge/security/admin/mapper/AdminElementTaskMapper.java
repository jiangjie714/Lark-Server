package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.AdminElementTask;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author  fansq
 * @since 20-4-13 4-15
 * @deprecation
 */
public interface AdminElementTaskMapper extends Mapper<AdminElementTask> {

    /**
     * 根据角色id,菜单id获取ResourceMap中的element
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     * @param type   资源类型
     * @return 元素列表
     */
    List<AdminElementTask> getAuthorityMenuElement(@Param("roleId") String roleId, @Param("menuId") String menuId,
                                                   @Param("type") String type);

    /**
     * 给task任务使用获取资源
     * @param roleId
     * @param type
     * @return
     */
    List<AdminElementTask> getAuthorityMenuElementTask(@Param("roleId") String roleId,
                                                   @Param("type") String type);
    /**
     * 根据角色Id获取权限下的Element
     * @param roleId 角色Id
     * @return
     */
    List<AdminElementTask> getElemntByRoleId(@Param("roleId")String roleId);
}