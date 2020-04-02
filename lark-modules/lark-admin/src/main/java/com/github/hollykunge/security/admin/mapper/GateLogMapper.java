package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.GateLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GateLogMapper extends Mapper<GateLog> {
    List<GateLog> gateLogExport();

    /**
     * fansq 获取指定部门的log数量
     * @param orgCode
     * @param menu
     * @return
     */
    Long getOrgCodeLogNum(@Param("orgCode") String orgCode,
                          @Param("type") String type,
                          @Param("menu") String menu);

    Long getCountLog(@Param("orgCode") String orgCode);

    /**
     * fansq 获取指定时间范围的数据量
     * @param type
     * @return
     */
    int getAccess(@Param("type") String type);
}