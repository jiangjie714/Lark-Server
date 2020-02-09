package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.PositionUserMap;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PositionUserMapMapper extends Mapper<PositionUserMap> {

    /**
     *  fansq 20-2-7 添加 导入excel
     * @param positionUserMaps 导入用户数据对应的权限信息
     */
    void insertExcelUserRole(@Param("userPositions") List<PositionUserMap> positionUserMaps);
}
