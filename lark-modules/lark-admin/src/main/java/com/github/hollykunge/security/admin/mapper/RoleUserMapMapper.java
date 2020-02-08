package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.RoleUserMap;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleUserMapMapper extends Mapper<RoleUserMap> {

    /**
     *  fansq 20-2-7 添加 导入excel
     * @param roleUserMaps 导入用户数据对应的角色信息
     */
    void insertExcelUserRole(@Param("userRoles") List<RoleUserMap> roleUserMaps);
}