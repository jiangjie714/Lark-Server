package com.github.hollykunge.servicewebservice.config.mq;

import com.github.hollykunge.security.common.constant.CommonConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.DirectExchange;

/**
 * 创建交换机
 * @Author: yzq
 * @Date: 创建于 2019/7/23 15:32
 */
@Configuration
public class ExchangeConfig {

    @Bean
    public DirectExchange adminUserAndOrgExchange(){
        DirectExchange directExchange = new DirectExchange(CommonConstants.WERSERVICE_ADMIN_USERANDORG_EXCHANGE,true,false);
        return directExchange;
    }
}
