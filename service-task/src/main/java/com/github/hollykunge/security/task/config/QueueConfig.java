package com.github.hollykunge.security.task.config;

import com.github.hollykunge.security.task.constant.TaskCommon;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation 队列配置
 */
@Configuration
public class QueueConfig {

    @Bean
    @Order(value = 1)
    public Queue inviteMemberDeadQueue() {
        return new Queue(TaskCommon.INVITEMEMBER_DEAD_QUEUE, true);
    }

    @Bean
    @Order(value = 2)
    public Queue inviteMemberQueue() {
        Map<String, Object> args = new HashMap<>(2);
        args.put(TaskCommon.DEAD_LETTER_QUEUE_KEY, TaskCommon.INVITEMEMBER_DEAD_EXCHANGENAME);
        args.put(TaskCommon.DEAD_LETTER_ROUTING_KEY, TaskCommon.INVITEMEMBER_DEAD_ROUTING_KEY);
        return new Queue(TaskCommon.INVITEMEMBER_QUEUE,true,false,false,args);
    }
}
