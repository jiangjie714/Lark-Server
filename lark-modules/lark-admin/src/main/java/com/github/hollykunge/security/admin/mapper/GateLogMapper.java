package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.GateLog;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GateLogMapper extends Mapper<GateLog> {
    List<GateLog> gateLogExport();
}