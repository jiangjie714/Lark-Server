package com.github.hollykunge.security.log;

import com.github.hollykunge.security.common.handler.GlobalExceptionHandler;
import com.github.hollykunge.security.log.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author: zhhongyu
 * @description: 启动服务类
 * @since: Create in 15:02 2020/2/27
 */
@EnableDiscoveryClient
@SpringBootApplication
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
    @Bean
    public SpringContextUtil getSpringContextUtil(){
        return new SpringContextUtil();
    }

    @Bean
    public GlobalExceptionHandler setGlobalExceptionHandler(){
        return new GlobalExceptionHandler();
    }
}
