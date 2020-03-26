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
     * @param startTime
     * @param endTime
     * @param menu
     * @return
     */
    Long getOrgCodeLogNum(@Param("orgCode") String orgCode,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime,
                          @Param("menu") String menu);
}