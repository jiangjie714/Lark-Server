package com.github.hollykunge.security.knowledge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.github.hollykunge.security"})
@MapperScan(basePackages = "com.github.hollykunge.security.knowledge.mapper")
@ComponentScan({"com.github.hollykunge.security.admin.api", "com.github.hollykunge.security.knowledge"})
public class ServiceKnowledgeBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ServiceKnowledgeBootstrap.class, args);
    }
}
