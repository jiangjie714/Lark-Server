package com.github.hollykunge.security;

import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import com.github.hollykunge.security.common.config.DruidConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * <p>author  zhhongyu </p><br>
 * <p>date 2019年5月8日</p>br>
 * <p>门户服务启动类</p>br>
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.github.hollykunge.security.mapper")
@Import(DruidConfig.class)
@EnableEncrypt
public class PortalBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(PortalBootStrap.class, args);
    }

    @Bean(name = "simpleRabbitListenerContainerFactory")
    @Qualifier
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置并发
        factory.setConcurrentConsumers(10);
        //最大并发
        factory.setMaxConcurrentConsumers(10);
        //消息接收——手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置超时
        factory.setReceiveTimeout(2000L);
        //设置重试间隔
        factory.setFailedDeclarationRetryInterval(3000L);
        //监听自定义格式转换
        //factory.setMessageConverter(jsonMessageConverter);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
