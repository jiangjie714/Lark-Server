package com.github.hollykunge.security.simulation.vo;

import lombok.Data;

@Data
public class UserSystemsVo {

    private String id;

    private String systemName;

    private String levels;

    private String systemDescribe;

    private String fileId;

    private String crtName;

    private String updTime;

    private String updName;

    private Boolean isCreator;

    private String creatorPId;

    private String users;

    private String state;

}
