package com.github.hollykunge.security.admin;

import com.ace.cache.EnableAceCache;
import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import com.github.hollykunge.security.auth.client.EnableAceAuthClient;
import com.github.hollykunge.security.common.config.DruidConfig;
import com.github.hollykunge.security.mq.EnableRabbitMq;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-05-25 12:44
 */
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.github.hollykunge.security"})
@EnableScheduling
@EnableAceAuthClient
@EnableAceCache
@EnableTransactionManagement
@MapperScan("com.github.hollykunge.security.admin.mapper")
@EnableSwagger2Doc
@Import(DruidConfig.class)
@ServletComponentScan
@EnableAspectJAutoProxy(exposeProxy=true)
@EnableHystrix
@EnableRabbitMq
@EnableEncrypt
public class AdminBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AdminBootstrap.class).web(true).run(args);    }
}
