package com.github.hollykunge.security.auth.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class SystemUserConfiguration {
    @Value("${sys.username}")
    private String sysUsername;
    @Value("${sys.password}")
    private String sysPassword;
    @Value("${sys.orgcode}")
    private String orgCode;
}