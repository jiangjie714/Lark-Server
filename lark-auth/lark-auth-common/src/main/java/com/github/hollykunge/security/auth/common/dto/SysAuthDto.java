package com.github.hollykunge.security.auth.common.dto;

import lombok.Data;

@Data
public class SysAuthDto {
    private String sysUsername;
    private String sysPassword;
    private String orgCode;
}