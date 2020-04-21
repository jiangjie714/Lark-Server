package com.github.hollykunge.security.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: fansq
 * @description: 任务管理启动类
 * @since: 20-4-13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@MapperScan(basePackages = "com.github.hollykunge.security.task.mapper")
public class ServiceTaskBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTaskBootstrap.class, args);
    }
}