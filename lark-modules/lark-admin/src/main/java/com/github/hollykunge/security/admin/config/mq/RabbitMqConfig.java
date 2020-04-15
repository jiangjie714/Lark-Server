package com.github.hollykunge.security.admin.config.mq;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Notice;
import com.github.hollykunge.security.admin.mapper.NoticeMapper;
import com.github.hollykunge.security.common.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * RabbitMq配置
 * @author zhhongyu
 * @create 2019/5/9 13:37
 */
@Configuration
@Slf4j
public class RabbitMqConfig {
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 连接工厂
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 定义rabbit template用于数据的接收和发送
     * @return
     */
    @Bean
    @Primary
    public RabbitTemplate noticeRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    Notice notice = new Notice();
                    notice.setIsSend("0");
                    notice.setId(correlationData.getId());
                    noticeMapper.updateByPrimaryKeySelective(notice);
                }
            }
        });
        return template;
    }

    @Bean
    @Qualifier("adminRabbitTemplate")
    public RabbitTemplate adminRabbitTemplate(){
        return  new RabbitTemplate(connectionFactory);
    }
}
