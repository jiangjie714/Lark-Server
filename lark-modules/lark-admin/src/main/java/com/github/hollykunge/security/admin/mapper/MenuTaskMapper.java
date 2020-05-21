package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.Menu;
import com.github.hollykunge.security.admin.entity.MenuTask;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MenuTaskMapper extends Mapper<MenuTask> {
    /**
     * 根据角色Id获取权限下的Element
     * @param roleId 角色Id
     * @return
     */
    List<MenuTask> selectMenuByRoleId(@Param("roleId") String roleId);
}