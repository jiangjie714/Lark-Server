package com.github.hollykunge.servicewebservice.config.mq;

import com.github.hollykunge.security.common.constant.CommonConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建交换机
 * @Author: yzq
 * @Date: 创建于 2019/7/23 15:56
 */
@Configuration
public class QueueConfig {

    @Bean
    public Queue adminUserQueue(){
        return new Queue(CommonConstants.ADMINUSER_QUEUE_NAME,true);
    }
    @Bean
    public Queue adminOrgQueue(){
        return new Queue(CommonConstants.ADMINORG_QUEUE_NAME,true);
    }

    /**
     * 推送协同编辑用户队列
     * @return
     */
    @Bean
    public Queue oneDocUserQueue(){
        return new Queue(CommonConstants.ONEDOCUSER_QUEUE_NAME,true);
    }

    /**
     * 推送协同编辑组织队列
     * @return
     */
    @Bean
    public Queue oneDocOrgQueue(){
        return new Queue(CommonConstants.ONEDOCORG_QUEUE_NAME,true);
    }
}
