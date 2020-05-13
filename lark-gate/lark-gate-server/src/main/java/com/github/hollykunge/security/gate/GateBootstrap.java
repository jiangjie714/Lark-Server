package com.github.hollykunge.security.gate;


import com.github.hollykunge.gate.ratelimit.EnableAceGateRateLimit;
import com.github.hollykunge.gate.ratelimit.config.IUserPrincipal;
import com.github.hollykunge.security.auth.client.EnableAceAuthClient;
import com.github.hollykunge.security.common.annotation.EnableLarkFeignAspect;
import com.github.hollykunge.security.gate.config.UserPrincipal;
import com.github.hollykunge.security.gate.utils.DBLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by 协同设计小组 on 2017/6/2.
 */
@EnableHystrix
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.github.hollykunge.security"})
@EnableZuulProxy
@EnableScheduling
@EnableAceAuthClient
@EnableAceGateRateLimit
@EnableLarkFeignAspect
public class GateBootstrap {
    public static void main(String[] args) {
        DBLog.getInstance().start();
        SpringApplication.run(GateBootstrap.class, args);
    }

    @Bean
    @Primary
    IUserPrincipal userPrincipal(){
        return new UserPrincipal();
    }
}
