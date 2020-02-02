package com.workhub.larktools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableCaching
@MapperScan("com.workhub.larktools.dao")
@EnableTransactionManagement
public class ServiceToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceToolsApplication.class, args);
    }

}
