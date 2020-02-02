package com.github.hollykunge.serviceunitproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@Configuration
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.github.hollykunge.serviceunitproject.dao")
public class ServiceunitprojectApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServiceunitprojectApplication.class, args);
    }

}
