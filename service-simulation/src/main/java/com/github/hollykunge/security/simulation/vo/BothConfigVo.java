package com.github.hollykunge.security.simulation.vo;

import com.github.hollykunge.security.simulation.entity.SystemResult;
import lombok.Data;

@Data
public class BothConfigVo {

    private String id;

    private String modelName;

    private String fileName;

    private SystemResult systemResult;

}
