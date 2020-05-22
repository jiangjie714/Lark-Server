package com.github.hollykunge.security.search;

import com.github.hollykunge.security.common.handler.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author: zhhongyu
 * @description: 启动服务类
 * @since: Create in 15:02 2020/2/27
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"com.github.hollykunge.security.search.*.feign"})
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

    @Bean
    public GlobalExceptionHandler setGlobalExceptionHandler(){
        return new GlobalExceptionHandler();
    }
}
