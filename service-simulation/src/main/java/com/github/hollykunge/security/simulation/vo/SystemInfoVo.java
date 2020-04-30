package com.github.hollykunge.security.simulation.vo;

import com.github.hollykunge.security.simulation.entity.SystemResult;
import lombok.Data;

@Data
public class SystemInfoVo {

    private String id;

    private String systemName;

    private String systemDescribe;

    private String modelName;

    private String fileName;

    private SystemResult systemResult;
}
