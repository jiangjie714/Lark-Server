package com.github.hollykunge.security.task.config;

import com.github.hollykunge.security.task.constant.TaskCommon;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation 交换机配置
 */
@Configuration
public class ExchangeConfig {

    /**
     * 创建死信交换机
     * @return
     */
    @Bean
    @Order(value = 1)
    public DirectExchange inviteMemberDeadExchange() {
        return new DirectExchange(TaskCommon.INVITEMEMBER_DEAD_QUEUE);
    }
    /**
     * name: 交换机名称
     * type: 交换机类型 direct，topic，fanout，headers
     * durability: 是否需要持久化，true 为持久化
     * auto delete: 当最后一个绑定到 exchange 上的队列被删除后，exchange 没有绑定的队列了，自动删除该 exchange
     * internal: 当前 exchange 是否用于 rabbitMQ 内部使用，默认为 false
     * arguments: 扩展参数，用于扩展 AMQP 协议自制定化使用
     * @return
     */
    @Bean
    @Order(value = 2)
    public DirectExchange inviteMemberExchange(){
        return new DirectExchange(TaskCommon.INVITEMEMBER_EXCHANGE, true, false);
    }


}
