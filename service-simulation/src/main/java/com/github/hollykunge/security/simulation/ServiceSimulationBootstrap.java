package com.github.hollykunge.security.simulation;

import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jihang
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.github.hollykunge.security"})
@MapperScan(basePackages = "com.github.hollykunge.security.simulation.mapper")
@ComponentScan({"com.github.hollykunge.security.admin.api", "com.github.hollykunge.security.simulation"})
@EnableEncrypt
public class ServiceSimulationBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSimulationBootstrap.class, args);
    }
}
