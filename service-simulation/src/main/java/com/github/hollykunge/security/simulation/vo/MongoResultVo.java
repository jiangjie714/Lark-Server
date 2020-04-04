package com.github.hollykunge.security.simulation.vo;

import com.github.hollykunge.security.simulation.entity.SystemResult;
import lombok.Data;

@Data
public class MongoResultVo {

    private String id;

    private String systemName;

    private String systemDescribe;

    private SystemResult systemResult;

}
