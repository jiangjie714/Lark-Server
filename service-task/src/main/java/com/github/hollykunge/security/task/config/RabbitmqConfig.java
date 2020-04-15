package com.github.hollykunge.security.task.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation rabbitmq配置类
 */
@Configuration
public class RabbitmqConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    @Qualifier("taskRabbitTemplate")
    public RabbitTemplate adminRabbitTemplate(){
        return  new RabbitTemplate(connectionFactory);
    }
}
