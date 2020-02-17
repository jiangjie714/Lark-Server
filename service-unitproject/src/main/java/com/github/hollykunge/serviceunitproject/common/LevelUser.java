package com.github.hollykunge.serviceunitproject.common;

import lombok.Data;

@Data
public class LevelUser {


    /**
     * 人员级别 1为副总师；2为技术负责人；3为参与人
     */
    private String levelType;


    /**
     * 人员身份证号
     */
    private String PId;

    /**
     * 人员身份证号
     */
    private String levelTypeDescript;


}

