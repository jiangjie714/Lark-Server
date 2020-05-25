package com.github.hollykunge.security.task;

import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import com.github.hollykunge.security.auth.client.EnableAceAuthClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: fansq
 * @description: 任务管理启动类
 * @since: 20-4-13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients({"com.github.hollykunge.security.auth.client.feign","com.github.hollykunge.security.task.feign"})
@MapperScan(basePackages = "com.github.hollykunge.security.task.mapper")
@ServletComponentScan
@EnableAceAuthClient
@EnableTransactionManagement
@EnableEncrypt
public class ServiceTaskBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTaskBootstrap.class, args);
    }
}