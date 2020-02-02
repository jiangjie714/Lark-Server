package com.github.hollykunge.security.auth.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class SysAuthConfig {
    private String sysUsername;
    private String sysPassword;
    private String sysOrgCode;
}